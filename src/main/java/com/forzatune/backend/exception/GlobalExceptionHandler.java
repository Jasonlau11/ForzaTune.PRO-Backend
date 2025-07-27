package com.forzatune.backend.exception;

import com.forzatune.backend.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 全局异常处理器
 * 统一处理各种异常，提供详细的错误日志输出
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理一般性异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception e, HttpServletRequest request) {
        logger.error("🚨 系统异常！请求路径: {}, 异常类型: {}, 错误信息: {}", 
            request.getRequestURI(), e.getClass().getSimpleName(), e.getMessage(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.failure("系统内部错误: " + e.getMessage()));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        logger.error("🔥 运行时异常！请求路径: {}, 错误信息: {}", 
            request.getRequestURI(), e.getMessage(), e);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure("操作失败: " + e.getMessage()));
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        logger.warn("🔒 访问被拒绝！请求路径: {}, 错误信息: {}", 
            request.getRequestURI(), e.getMessage());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.failure("无权限访问: " + e.getMessage()));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringBuilder errorMsg = new StringBuilder("参数验证失败: ");
        e.getBindingResult().getFieldErrors().forEach(error -> 
            errorMsg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        
        logger.warn("📝 参数验证失败！请求路径: {}, 验证错误: {}", 
            request.getRequestURI(), errorMsg.toString());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(errorMsg.toString()));
    }

    /**
     * 处理参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMsg = String.format("参数类型错误: %s 应该是 %s 类型", 
            e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        
        logger.warn("🔄 参数类型转换异常！请求路径: {}, 错误信息: {}", 
            request.getRequestURI(), errorMsg);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(errorMsg));
    }

    /**
     * 处理数据库异常
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<Object>> handleSQLException(SQLException e, HttpServletRequest request) {
        logger.error("💾 数据库异常！请求路径: {}, SQL错误码: {}, 错误信息: {}", 
            request.getRequestURI(), e.getErrorCode(), e.getMessage(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.failure("数据库操作失败"));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        logger.warn("⚠️  非法参数异常！请求路径: {}, 错误信息: {}", 
            request.getRequestURI(), e.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure("参数错误: " + e.getMessage()));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        logger.error("🎯 空指针异常！请求路径: {}, 堆栈信息:", 
            request.getRequestURI(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.failure("系统内部错误，请联系管理员"));
    }
} 