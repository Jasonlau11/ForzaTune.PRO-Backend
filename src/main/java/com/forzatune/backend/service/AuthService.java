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

    /**
     * 用户登录
     */
    public AuthResponse login(LoginRequest request) {
        logger.info("🔐 用户登录尝试: {}", request.getEmail());

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            logger.warn("⚠️ 用户不存在: {}", request.getEmail());
            throw new RuntimeException("邮箱或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPass(), user.getPasswordHash())) {
            logger.warn("⚠️ 密码错误: {}", request.getEmail());
            throw new RuntimeException("邮箱或密码错误");
        }

        // 检查用户状态
        if (!user.getIsActive()) {
            logger.warn("⚠️ 用户已被禁用: {}", request.getEmail());
            throw new RuntimeException("账户已被禁用");
        }

        // 更新最后登录时间
        user.setLastLogin(LocalDateTime.now());
        userMapper.updateUser(user);

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        // 构建响应
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getXboxId(),
                user.getIsProPlayer(),
                user.getXboxId() != null && !user.getXboxId().isEmpty(),
                user.getUserTier().name()
        );

        logger.info("✅ 用户登录成功: {} ({})", user.getXboxId(), request.getEmail());
        return new AuthResponse(token, userInfo);
    }

    /**
     * 用户注册
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("📝 用户注册尝试: {} ({})", request.getXboxId(), request.getEmail());

        // 检查邮箱是否已存在
        if (userMapper.findByEmail(request.getEmail()) != null) {
            logger.warn("⚠️ 邮箱已存在: {}", request.getEmail());
            throw new RuntimeException("该邮箱已被注册");
        }

        // 检查Xbox ID是否已存在
        if (userMapper.findByXboxId(request.getXboxId()) != null) {
            logger.warn("⚠️ Xbox ID已存在: {}", request.getXboxId());
            throw new RuntimeException("该Xbox ID已被使用");
        }

        // 创建新用户
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPass()));
        user.setXboxId(request.getXboxId());
        user.setIsProPlayer(false);
        user.setTotalTunes(0);
        user.setTotalLikes(0);
        user.setUserTier(User.UserTier.STANDARD);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        // 插入数据库
        int result = userMapper.insertUser(user);
        if (result <= 0) {
            logger.error("❌ 用户注册失败: {}", request.getEmail());
            throw new RuntimeException("注册失败，请稍后重试");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        // 构建响应
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getXboxId(),
                user.getIsProPlayer(),
                false, // 新用户没有链接Xbox ID
                user.getUserTier().name()
        );

        logger.info("✅ 用户注册成功: {} ({})", user.getXboxId(), request.getEmail());
        return new AuthResponse(token, userInfo);
    }

    /**
     * 验证JWT token并获取用户信息
     */
    public User validateTokenAndGetUser(String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return null;
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return null;
            }

            User user = userMapper.findById(userId);
            if (user == null || !user.getIsActive()) {
                return null;
            }

            return user;
        } catch (Exception e) {
            logger.error("Token验证失败: {}", e.getMessage());
            return null;
        }
    }

} 