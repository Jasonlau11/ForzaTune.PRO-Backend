package com.forzatune.backend.service;

import com.forzatune.backend.dto.AuthResponse;
import com.forzatune.backend.dto.LoginRequest;
import com.forzatune.backend.dto.RegisterRequest;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.mapper.UserMapper;
import com.forzatune.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 认证服务
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final CacheManager cacheManager;

    /**
     * 用户登录
     */
    public AuthResponse login(LoginRequest request) {
        logger.info("🔐 处理登录请求: {}", request.getEmail());
        
        // 查找用户
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            logger.warn("⚠️ 用户不存在: {}", request.getEmail());
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPass(), user.getPassword())) {
            logger.warn("⚠️ 密码错误: {}", request.getEmail());
            throw new RuntimeException("密码错误");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user);

        // 构建响应
        AuthResponse.User userInfo = new AuthResponse.User(
                user.getId(),
                user.getEmail(),
                user.getXboxId(),
                user.getIsProPlayer(),
                user.getXboxId() != null && !user.getXboxId().isEmpty()
        );

        // 更新最近登录时间
        user.setLastLogin(LocalDateTime.now());
        userMapper.updateUser(user);

        logger.info("✅ 登录成功: {}", request.getEmail());
        return new AuthResponse(token, userInfo);
    }

    /**
     * 解除绑定当前用户的 Xbox ID
     * @param token JWT token
     * @return 更新后的用户信息
     */
    public AuthResponse.UserInfo unlinkXbox(String token) {
        String userId = jwtUtil.validateToken(token);
        if (userId == null) {
            throw new RuntimeException("未认证");
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setXboxId("");
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);

        return new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getXboxId(),
                user.getIsProPlayer(),
                user.getXboxId() != null && !user.getXboxId().isEmpty(),
                user.getUserTier().name()
        );
    }

    /**
     * 用户注册
     */
    public AuthResponse register(RegisterRequest request) {
        logger.info("📝 处理注册请求: {} ({})", request.getXboxId(), request.getEmail());
        
        // 验证密码确认
        if (!request.getPass().equals(request.getConfirmPass())) {
            logger.warn("⚠️ 密码确认不匹配: {}", request.getEmail());
            throw new RuntimeException("密码和确认密码不匹配");
        }
        
        // 验证密码强度
        if (!isPasswordStrong(request.getPass())) {
            logger.warn("⚠️ 密码强度不足: {}", request.getEmail());
            throw new RuntimeException("密码必须包含至少6个字符，包含字母和数字");
        }
        
        // 验证Xbox ID格式
        if (!isValidXboxId(request.getXboxId())) {
            logger.warn("⚠️ Xbox ID格式不正确: {}", request.getXboxId());
            throw new RuntimeException("Xbox ID格式不正确，只能包含字母、数字和下划线");
        }

        // 检查邮箱是否已存在
        if (userMapper.findByEmail(request.getEmail()) != null) {
            logger.warn("⚠️ 邮箱已存在: {}", request.getEmail());
            throw new RuntimeException("邮箱已存在");
        }

        // 检查Xbox ID是否已存在
        if (userMapper.findByXboxId(request.getXboxId()) != null) {
            logger.warn("⚠️ Xbox ID已存在: {}", request.getXboxId());
            throw new RuntimeException("Xbox ID已存在");
        }

        // 校验邮箱验证码
        verifyEmailCodeOrThrow(request.getEmail(), request.getEmailCode());

        // 创建新用户
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(request.getEmail());
        newUser.setXboxId(request.getXboxId());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPass()));
        newUser.setIsProPlayer(false);
        newUser.setUserTier(User.UserTier.STANDARD);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setEmailVerifiedAt(LocalDateTime.now());

        // 保存用户
        userMapper.insert(newUser);

        // 生成JWT token
        String token = jwtUtil.generateToken(newUser);

        // 构建响应
        AuthResponse.User userInfo = new AuthResponse.User(
                newUser.getId(),
                newUser.getEmail(),
                newUser.getXboxId(),
                newUser.getIsProPlayer(),
                newUser.getXboxId() != null && !newUser.getXboxId().isEmpty()
        );

        logger.info("✅ 注册成功: {} ({})", request.getXboxId(), request.getEmail());
        return new AuthResponse(token, userInfo);
    }

    /**
     * 发送邮箱验证码
     */
    public void sendEmailCode(String email, String clientIp) {
        validateEmailFormat(email);

        Cache cooldownCache = cacheManager.getCache("emailSendCooldown");
        Cache dailyQuotaCache = cacheManager.getCache("emailDailyQuota");
        Cache verificationCache = cacheManager.getCache("emailVerification");

        String cooldownKeyEmail = "cooldown:" + email;
        String cooldownKeyIp = clientIp != null ? "cooldownIP:" + clientIp : null;

        if (cooldownCache != null && cooldownCache.get(cooldownKeyEmail) != null) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }
        if (cooldownKeyIp != null && cooldownCache != null && cooldownCache.get(cooldownKeyIp) != null) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }

        // 每日配额
        String quotaKey = "quota:" + email + ":" + java.time.LocalDate.now();
        Integer quota = dailyQuotaCache != null && dailyQuotaCache.get(quotaKey, Integer.class) != null
                ? dailyQuotaCache.get(quotaKey, Integer.class)
                : 0;
        if (quota >= 10) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }

        // 生成6位验证码
        String code = String.format("%06d", new java.util.Random().nextInt(1000000));

        // 缓存验证码
        if (verificationCache != null) {
            verificationCache.put("email:" + email, code);
        }

        // 发送邮件
        emailService.sendVerificationCode(email, code);

        // 设置冷却与配额+1
        if (cooldownCache != null) {
            cooldownCache.put(cooldownKeyEmail, true);
            if (cooldownKeyIp != null) cooldownCache.put(cooldownKeyIp, true);
        }
        if (dailyQuotaCache != null) {
            dailyQuotaCache.put(quotaKey, quota + 1);
        }

        logger.info("📨 已下发验证码到邮箱: {}", maskEmail(email));
    }

    private void verifyEmailCodeOrThrow(String email, String code) {
        if (code == null || !code.matches("\\d{6}")) {
            throw new RuntimeException("验证码无效或已过期");
        }
        Cache verificationCache = cacheManager.getCache("emailVerification");
        Cache failCache = cacheManager.getCache("emailCodeFailCount");
        String key = "email:" + email;
        String cached = verificationCache != null ? verificationCache.get(key, String.class) : null;

        if (cached == null) {
            throw new RuntimeException("验证码无效或已过期");
        }

        if (!code.equals(cached)) {
            String failKey = "fail:" + email;
            Integer fail = failCache != null && failCache.get(failKey, Integer.class) != null
                    ? failCache.get(failKey, Integer.class)
                    : 0;
            fail = fail + 1;
            if (failCache != null) failCache.put(failKey, fail);
            if (fail >= 5) {
                // 清除验证码，进入锁定期
                verificationCache.evict(key);
                throw new RuntimeException("验证码尝试过多，请稍后再试");
            }
            throw new RuntimeException("验证码无效或已过期");
        }

        // 验证成功：清理验证码与失败计数
        verificationCache.evict(key);
        Cache failCache2 = cacheManager.getCache("emailCodeFailCount");
        if (failCache2 != null) failCache2.evict("fail:" + email);
    }

    private void validateEmailFormat(String email) {
        if (email == null || !email.matches("^.+@.+\\..+$")) {
            throw new RuntimeException("邮箱格式不正确");
        }
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return "***" + email.substring(atIndex);
        String prefix = email.substring(0, Math.min(2, atIndex));
        return prefix + "***" + email.substring(atIndex);
    }

    /**
     * 验证token并获取用户信息
     */
    public User validateTokenAndGetUser(String token) {
        try {
            String userId = jwtUtil.validateToken(token);
            if (userId != null) {
                return userMapper.findById(userId);
            }
        } catch (Exception e) {
            logger.warn("⚠️ Token验证失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 验证密码强度
     */
    private boolean isPasswordStrong(String password) {
        return password.length() >= 6 && 
               password.matches(".*[a-zA-Z].*") && 
               password.matches(".*\\d.*");
    }

    /**
     * 验证Xbox ID格式
     */
    private boolean isValidXboxId(String xboxId) {
        return xboxId.matches("^[a-zA-Z0-9_]{3,50}$");
    }
} 