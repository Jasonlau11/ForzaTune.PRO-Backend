package com.forzatune.backend.controller;

import com.forzatune.backend.dto.AuthResponse;
import com.forzatune.backend.dto.LoginRequest;
import com.forzatune.backend.dto.RegisterRequest;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    /**
     * 用户登录
     * URL: POST /api/auth/login
     * 前端传参: { email: string, pass: string }
     * 后端返回: { token: string, user: User }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("🔐 收到登录请求: {}", request.getEmail());
        
        AuthResponse authResponse = authService.login(request);
        logger.info("✅ 登录成功: {}", request.getEmail());
        return ResponseEntity.ok(authResponse);
    }

    /**
     * 解除当前用户的 Xbox 绑定
     * URL: POST /api/auth/xbox/unlink
     * Header: Authorization: Bearer {token}
     * 返回: 最新的用户信息 UserInfo
     */
    @PostMapping("/xbox/unlink")
    public ResponseEntity<AuthResponse.UserInfo> unlinkXbox(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.replace("Bearer ", "");
            AuthResponse.UserInfo updated = authService.unlinkXbox(actualToken);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("❌ 解除Xbox绑定失败: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 用户注册
     * URL: POST /api/auth/register
     * 前端传参: { email: string, xboxId: string, pass: string, confirmPass: string }
     * 后端返回: { token: string, user: User }
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("📝 收到注册请求: {} ({})", request.getXboxId(), request.getEmail());
        
        AuthResponse authResponse = authService.register(request);
        logger.info("✅ 注册成功: {} ({})", request.getXboxId(), request.getEmail());
        return ResponseEntity.ok(authResponse);
    }

    /**
     * 发送邮箱验证码
     * URL: POST /api/auth/send-email-code
     * 前端传参: { email: string }
     * 后端返回: { success: true }
     */
    @PostMapping("/send-email-code")
    public ResponseEntity<?> sendEmailCode(@RequestBody Map<String, String> body, @RequestHeader(value = "X-Forwarded-For", required = false) String xff, @RequestHeader(value = "X-Real-IP", required = false) String realIp) {
        String email = body.get("email");
        String clientIp = realIp != null && !realIp.isEmpty() ? realIp : (xff != null && !xff.isEmpty() ? xff.split(",")[0].trim() : null);
        try {
            authService.sendEmailCode(email, clientIp);
            return ResponseEntity.ok(com.forzatune.backend.dto.ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(com.forzatune.backend.dto.ApiResponse.failure(e.getMessage()));
        }
    }

    /**
     * 获取当前用户信息
     * URL: GET /api/auth/profile
     * 前端传参: Authorization: Bearer {token}
     * 后端返回: UserInfo
     */
    @GetMapping("/profile")
    public ResponseEntity<AuthResponse.UserInfo> getProfile(
            @RequestHeader("Authorization") String token) {
        logger.info("👤 获取用户信息");
        
        try {
            // 移除Bearer前缀
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            if (user == null) {
                logger.warn("⚠️ 用户未认证或token无效");
                return ResponseEntity.status(401).build();
            }
            
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getXboxId(),
                    user.getIsProPlayer(),
                    user.getXboxId() != null && !user.getXboxId().isEmpty(),
                    user.getUserTier().name(),
                    user.getProPlayerSince() != null ? user.getProPlayerSince().toString() : null
            );
            
            logger.info("✅ 获取用户信息成功: {}", user.getEmail());
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            logger.error("❌ 获取用户信息失败: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 用户登出
     * URL: POST /api/auth/logout
     * 前端传参: Authorization: Bearer {token}
     * 后端返回: { message: string }
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        logger.info("🚪 用户登出");

        try {
            // 移除Bearer前缀并简单记录长度以避免未使用变量告警
            String actualToken = token.replace("Bearer ", "");
            logger.debug("Parsed token length: {}", actualToken.length());
            // 这里可以添加token到黑名单的逻辑
            logger.info("✅ 登出成功");
            return ResponseEntity.ok("登出成功");
        } catch (Exception e) {
            logger.error("❌ 登出失败: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 发送忘记密码验证码
     * URL: POST /api/auth/forgot-password
     * 前端传参: { email: string }
     * 后端返回: { success: true }
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            authService.sendForgotPasswordCode(email);
            return ResponseEntity.ok(com.forzatune.backend.dto.ApiResponse.success(true));
        } catch (Exception e) {
            logger.error("❌ 发送忘记密码验证码失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(com.forzatune.backend.dto.ApiResponse.failure(e.getMessage()));
        }
    }

    /**
     * 验证忘记密码验证码
     * URL: POST /api/auth/verify-reset-code
     * 前端传参: { email: string, code: string }
     * 后端返回: { success: true, token: string }
     */
    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        try {
            String resetToken = authService.verifyResetCode(email, code);
            return ResponseEntity.ok(com.forzatune.backend.dto.ApiResponse.success(
                Collections.singletonMap("token", resetToken)
            ));
        } catch (Exception e) {
            logger.error("❌ 验证重置密码验证码失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(com.forzatune.backend.dto.ApiResponse.failure(e.getMessage()));
        }
    }

    /**
     * 重置密码
     * URL: POST /api/auth/reset-password
     * 前端传参: { token: string, newPassword: string, confirmPassword: string }
     * 后端返回: { success: true }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");
        try {
            authService.resetPassword(token, newPassword, confirmPassword);
            return ResponseEntity.ok(com.forzatune.backend.dto.ApiResponse.success(true));
        } catch (Exception e) {
            logger.error("❌ 重置密码失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(com.forzatune.backend.dto.ApiResponse.failure(e.getMessage()));
        }
    }
    
    /**
     * 临时测试端点 - 测试密码加密
     */
    @PostMapping("/test-password")
    public ResponseEntity<?> testPassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        if (password == null) {
            password = "12345678";
        }
        
        logger.info("🧪 开始测试密码加密逻辑");
        authService.testPasswordEncryption(password);
        
        return ResponseEntity.ok(Collections.singletonMap("message", "测试完成，请查看日志"));
    }
} 