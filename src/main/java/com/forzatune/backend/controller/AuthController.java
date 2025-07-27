package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.AuthResponse;
import com.forzatune.backend.dto.LoginRequest;
import com.forzatune.backend.dto.RegisterRequest;
import com.forzatune.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("🔐 收到登录请求: {}", request.getEmail());
        
        try {
            AuthResponse authResponse = authService.login(request);
            logger.info("✅ 登录成功: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success(authResponse));
        } catch (Exception e) {
            logger.error("❌ 登录失败: {}, 错误: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("📝 收到注册请求: {} ({})", request.getXboxId(), request.getEmail());
        
        try {
            AuthResponse authResponse = authService.register(request);
            logger.info("✅ 注册成功: {} ({})", request.getXboxId(), request.getEmail());
            return ResponseEntity.ok(ApiResponse.success(authResponse));
        } catch (Exception e) {
            logger.error("❌ 注册失败: {}, 错误: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));
        }
    }

    /**
     * 用户登出
     * 注意：JWT是无状态的，实际的登出逻辑在前端处理（删除token）
     * 这里主要是为了日志记录和未来可能的扩展（如黑名单机制）
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        logger.info("🚪 用户登出");
        return ResponseEntity.ok(ApiResponse.success("登出成功"));
    }

} 