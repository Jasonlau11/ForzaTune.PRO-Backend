package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.entity.Notification;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.service.AuthService;
import com.forzatune.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    private final NotificationService notificationService;
    private final AuthService authService;
    
    /**
     * 获取用户通知列表
     */
    @GetMapping
    public ResponseEntity<?> getUserNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能查看通知"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            if (user == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 获取通知列表
            List<Notification> notifications = notificationService.getUserNotifications(user.getId(), page, size);
            
            logger.info("✅ 获取用户通知列表成功: userId={}, page={}, size={}, count={}", 
                    user.getId(), page, size, notifications.size());
            
            return ResponseEntity.ok(ApiResponse.success(notifications));
            
        } catch (Exception e) {
            logger.error("❌ 获取通知列表失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("获取通知列表失败"));
        }
    }
    
    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadNotificationCount(
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能查看通知"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            if (user == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 获取未读通知数量
            int unreadCount = notificationService.getUnreadNotificationCount(user.getId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("unreadCount", unreadCount);
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (Exception e) {
            logger.error("❌ 获取未读通知数量失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("获取未读通知数量失败"));
        }
    }
    
    /**
     * 标记通知为已读
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能操作通知"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            if (user == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 标记通知为已读
            boolean success = notificationService.markNotificationAsRead(id, user.getId());
            
            if (success) {
                logger.info("✅ 标记通知为已读成功: userId={}, notificationId={}", user.getId(), id);
                return ResponseEntity.ok(ApiResponse.success("标记成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.failure("通知不存在或无权限"));
            }
            
        } catch (Exception e) {
            logger.error("❌ 标记通知为已读失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("标记失败"));
        }
    }
    
    /**
     * 标记所有通知为已读
     */
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllNotificationsAsRead(
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能操作通知"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            if (user == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 标记所有通知为已读
            boolean success = notificationService.markAllNotificationsAsRead(user.getId());
            
            if (success) {
                logger.info("✅ 标记所有通知为已读成功: userId={}", user.getId());
                return ResponseEntity.ok(ApiResponse.success("全部标记成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.failure("标记失败"));
            }
            
        } catch (Exception e) {
            logger.error("❌ 标记所有通知为已读失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("标记失败"));
        }
    }
    
    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能操作通知"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            if (user == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 删除通知
            boolean success = notificationService.deleteNotification(id, user.getId());
            
            if (success) {
                logger.info("✅ 删除通知成功: userId={}, notificationId={}", user.getId(), id);
                return ResponseEntity.ok(ApiResponse.success("删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.failure("通知不存在或无权限"));
            }
            
        } catch (Exception e) {
            logger.error("❌ 删除通知失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("删除失败"));
        }
    }
}
