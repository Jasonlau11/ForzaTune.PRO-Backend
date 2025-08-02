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

        logger.info("✅ 登录成功: {}", request.getEmail());
        return new AuthResponse(token, userInfo);
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

        // 创建新用户
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(request.getEmail());
        newUser.setXboxId(request.getXboxId());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPass()));
        newUser.setIsProPlayer(false);
        newUser.setUserTier(User.UserTier.STANDARD);

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