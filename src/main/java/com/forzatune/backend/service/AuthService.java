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
import org.springframework.transaction.annotation.Transactional;

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
        logger.debug("🔍 调试信息 - 用户: {}, 输入密码: {}, 存储密码哈希: {}", 
                    request.getEmail(), request.getPass(), user.getPassword());
        
        boolean passwordMatches = passwordEncoder.matches(request.getPass(), user.getPassword());
        logger.debug("🔍 密码匹配结果: {}", passwordMatches);
        
        if (!passwordMatches) {
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
                user.getUserTier().name(),
                user.getProPlayerSince() != null ? user.getProPlayerSince().toString() : null
        );
    }

    /**
     * 用户注册
     */
    @Transactional
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
        
        String encodedPassword = passwordEncoder.encode(request.getPass());
        logger.debug("🔍 注册调试信息 - 用户: {}, 原始密码: {}, 加密后密码: {}", 
                    request.getEmail(), request.getPass(), encodedPassword);
        
        newUser.setPasswordHash(encodedPassword);
        newUser.setIsProPlayer(false);
        newUser.setUserTier(User.UserTier.STANDARD);
        newUser.setTotalTunes(0);
        newUser.setTotalLikes(0);
        newUser.setIsActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setEmailVerifiedAt(LocalDateTime.now());
        
        logger.debug("🔍 用户对象详情: id={}, email={}, xboxId={}, isProPlayer={}, userTier={}, totalTunes={}, totalLikes={}, isActive={}", 
                    newUser.getId(), newUser.getEmail(), newUser.getXboxId(), newUser.getIsProPlayer(), 
                    newUser.getUserTier(), newUser.getTotalTunes(), newUser.getTotalLikes(), newUser.getIsActive());

        // 保存用户
        logger.debug("🔍 准备插入用户到数据库: {}", newUser.getEmail());
        int insertResult = userMapper.insert(newUser);
        logger.debug("🔍 用户插入结果: {}", insertResult);
        
        if (insertResult <= 0) {
            logger.error("❌ 用户插入失败: {}", newUser.getEmail());
            throw new RuntimeException("用户注册失败，请稍后重试");
        }
        
        // 验证用户是否真的插入成功
        User savedUser = userMapper.findByEmail(newUser.getEmail());
        if (savedUser == null) {
            logger.error("❌ 用户插入后查询不到: {}", newUser.getEmail());
            throw new RuntimeException("用户注册失败，请稍后重试");
        }
        logger.debug("🔍 用户插入验证成功: {}", savedUser.getId());

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

    /**
     * 发送忘记密码验证码
     */
    public void sendForgotPasswordCode(String email) {
        logger.info("🔐 发送忘记密码验证码: {}", email);

        validateEmailFormat(email);

        // 检查用户是否存在
        User user = userMapper.findByEmail(email);
        if (user == null) {
            logger.warn("⚠️ 用户不存在: {}", email);
            // 为了安全，不暴露用户是否存在的信息
            throw new RuntimeException("如果该邮箱已注册，我们将发送重置密码的验证码");
        }

        Cache cooldownCache = cacheManager.getCache("emailSendCooldown");
        Cache dailyQuotaCache = cacheManager.getCache("emailDailyQuota");
        Cache verificationCache = cacheManager.getCache("passwordReset");

        String cooldownKey = "cooldown:forgot:" + email;

        // 检查冷却时间
        if (cooldownCache != null && cooldownCache.get(cooldownKey) != null) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }

        // 每日配额检查
        String quotaKey = "quota:forgot:" + email + ":" + java.time.LocalDate.now();
        Integer quota = dailyQuotaCache != null && dailyQuotaCache.get(quotaKey, Integer.class) != null
                ? dailyQuotaCache.get(quotaKey, Integer.class)
                : 0;
        if (quota >= 5) {
            throw new RuntimeException("今日重置密码次数已达上限");
        }

        // 生成6位验证码
        String code = String.format("%06d", new java.util.Random().nextInt(1000000));

        // 缓存验证码（5分钟过期）
        if (verificationCache != null) {
            verificationCache.put("forgot:" + email, code);
        }

        // 发送邮件
        emailService.sendPasswordResetCode(email, code);

        // 设置冷却时间（1分钟）
        if (cooldownCache != null) {
            cooldownCache.put(cooldownKey, true);
        }

        // 更新配额
        if (dailyQuotaCache != null) {
            dailyQuotaCache.put(quotaKey, quota + 1);
        }

        logger.info("✅ 忘记密码验证码已发送: {}", maskEmail(email));
    }

    /**
     * 验证重置密码验证码
     */
    public String verifyResetCode(String email, String code) {
        logger.info("🔐 验证重置密码验证码: {}", email);

        if (code == null || !code.matches("\\d{6}")) {
            throw new RuntimeException("验证码格式不正确");
        }

        Cache verificationCache = cacheManager.getCache("passwordReset");
        String key = "forgot:" + email;
        String cached = verificationCache != null ? verificationCache.get(key, String.class) : null;

        if (cached == null) {
            throw new RuntimeException("验证码无效或已过期");
        }

        if (!code.equals(cached)) {
            throw new RuntimeException("验证码错误");
        }

        // 验证成功，生成临时重置token（有效期10分钟）
        String resetToken = UUID.randomUUID().toString();

        // 清理验证码并存储重置token（使用token作为key，email作为value）
        verificationCache.evict(key);
        if (verificationCache != null) {
            verificationCache.put("token:" + resetToken, email);
        }

        logger.info("✅ 重置密码验证码验证成功: {}", maskEmail(email));
        return resetToken;
    }

    /**
     * 重置密码
     */
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        logger.info("🔐 重置密码");

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("重置令牌无效");
        }

        // 验证密码
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("密码长度至少为6位");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        if (!isPasswordStrong(newPassword)) {
            throw new RuntimeException("密码必须包含字母和数字");
        }

        // 查找对应的用户邮箱
        Cache verificationCache = cacheManager.getCache("passwordReset");
        String email = null;

        if (verificationCache != null) {
            // 直接通过token获取email
            email = verificationCache.get("token:" + token, String.class);
        }

        if (email == null) {
            throw new RuntimeException("重置令牌无效或已过期");
        }

        // 查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新密码
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);

        // 清理缓存
        if (verificationCache != null) {
            verificationCache.evict("token:" + token);
        }

        logger.info("✅ 密码重置成功: {}", maskEmail(email));
    }
    
    /**
     * 临时测试方法 - 验证密码加密和验证逻辑
     */
    public void testPasswordEncryption(String plainPassword) {
        logger.info("🧪 测试密码加密 - 原始密码: {}", plainPassword);
        
        // 加密密码
        String encoded1 = passwordEncoder.encode(plainPassword);
        String encoded2 = passwordEncoder.encode(plainPassword);
        
        logger.info("🧪 第一次加密结果: {}", encoded1);
        logger.info("🧪 第二次加密结果: {}", encoded2);
        
        // 验证密码
        boolean match1 = passwordEncoder.matches(plainPassword, encoded1);
        boolean match2 = passwordEncoder.matches(plainPassword, encoded2);
        boolean crossMatch = passwordEncoder.matches(plainPassword, encoded1);
        
        logger.info("🧪 原始密码与第一次加密匹配: {}", match1);
        logger.info("🧪 原始密码与第二次加密匹配: {}", match2);
        logger.info("🧪 交叉验证: {}", crossMatch);
        
        // 测试错误密码
        boolean wrongMatch = passwordEncoder.matches("wrongpassword", encoded1);
        logger.info("🧪 错误密码匹配测试: {}", wrongMatch);
    }
} 