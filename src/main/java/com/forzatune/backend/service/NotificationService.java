package com.forzatune.backend.service;

import com.forzatune.backend.entity.Notification;
import com.forzatune.backend.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 通知服务类
 */
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    private final NotificationMapper notificationMapper;
    
    /**
     * 发送通知
     */
    @Transactional
    public void sendNotification(String userId, Notification.NotificationType type, 
                               String title, String content, String relatedId, 
                               String senderId, String senderXboxId) {
        try {
            logger.debug("🔔 开始发送通知 - 接收者: {}, 类型: {}, 发送者: {}", userId, type, senderXboxId);
            
            // 检查是否已存在相同的通知（24小时内）
            Notification existingNotification = notificationMapper.selectNotificationByRelatedIdAndType(
                    userId, relatedId, type.getCode(), senderId);
            
            if (existingNotification != null) {
                logger.debug("⚠️ 通知已存在，跳过发送: userId={}, type={}, relatedId={}", userId, type, relatedId);
                return;
            }
            
            logger.debug("🔔 创建新通知对象 - 标题: {}", title);
            
            // 创建新通知
            Notification notification = new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setUserId(userId);
            notification.setType(type);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setRelatedId(relatedId);
            notification.setSenderId(senderId);
            notification.setSenderXboxId(senderXboxId);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());
            
            logger.debug("🔔 准备插入通知到数据库 - 通知ID: {}", notification.getId());
            
            int result = notificationMapper.insertNotification(notification);
            if (result > 0) {
                logger.info("✅ 通知发送成功: userId={}, type={}, title={}", userId, type, title);
            } else {
                logger.warn("⚠️ 通知发送失败: userId={}, type={}", userId, type);
            }
            
        } catch (Exception e) {
            logger.error("❌ 发送通知异常: userId={}, type={}, error={}", userId, type, e.getMessage(), e);
        }
    }
    
    /**
     * 发送调校点赞通知
     */
    public void sendTuneLikeNotification(String tuneOwnerId, String tuneId, String shareCode, 
                                       String likerId, String likerXboxId) {
        if (tuneOwnerId.equals(likerId)) {
            return; // 不给自己发通知
        }
        
        String title = "调校获得点赞";
        String content = String.format("%s 点赞了您的调校 %s", likerXboxId, shareCode);
        
        sendNotification(tuneOwnerId, Notification.NotificationType.TUNE_LIKE, 
                        title, content, tuneId, likerId, likerXboxId);
    }
    
    /**
     * 发送调校收藏通知
     */
    public void sendTuneFavoriteNotification(String tuneOwnerId, String tuneId, String shareCode,
                                           String favoriterId, String favoriterXboxId) {
        if (tuneOwnerId.equals(favoriterId)) {
            return; // 不给自己发通知
        }
        
        String title = "调校被收藏";
        String content = String.format("%s 收藏了您的调校 %s", favoriterXboxId, shareCode);
        
        sendNotification(tuneOwnerId, Notification.NotificationType.TUNE_FAVORITE,
                        title, content, tuneId, favoriterId, favoriterXboxId);
    }
    
    /**
     * 发送调校评论通知
     */
    public void sendTuneCommentNotification(String tuneOwnerId, String tuneId, String shareCode,
                                          String commenterId, String commenterXboxId, String commentContent) {
        logger.debug("📨 开始发送调校评论通知 - 调校拥有者: {}, 评论者: {}, 调校: {}", 
                    tuneOwnerId, commenterXboxId, shareCode);
        
        if (tuneOwnerId.equals(commenterId)) {
            logger.debug("⚠️ 跳过通知发送 - 不给自己发通知");
            return; // 不给自己发通知
        }
        
        String title = "调校收到新评论";
        String content = String.format("%s 评论了您的调校 %s: %s", 
                commenterXboxId, shareCode, 
                commentContent.length() > 50 ? commentContent.substring(0, 50) + "..." : commentContent);
        
        logger.debug("📨 准备发送通知 - 标题: {}, 内容: {}", title, content);
        
        sendNotification(tuneOwnerId, Notification.NotificationType.TUNE_COMMENT,
                        title, content, tuneId, commenterId, commenterXboxId);
        
        logger.debug("✅ 调校评论通知发送完成");
    }
    
    /**
     * 发送评论回复通知
     */
    public void sendCommentReplyNotification(String commentOwnerId, String commentId, 
                                           String replierId, String replierXboxId, String replyContent) {
        if (commentOwnerId.equals(replierId)) {
            return; // 不给自己发通知
        }
        
        String title = "评论收到回复";
        String content = String.format("%s 回复了您的评论: %s", 
                replierXboxId,
                replyContent.length() > 50 ? replyContent.substring(0, 50) + "..." : replyContent);
        
        sendNotification(commentOwnerId, Notification.NotificationType.COMMENT_REPLY,
                        title, content, commentId, replierId, replierXboxId);
    }
    
    /**
     * 获取用户通知列表
     */
    public List<Notification> getUserNotifications(String userId, int page, int size) {
        int offset = (page - 1) * size;
        return notificationMapper.selectNotificationsByUserId(userId, offset, size);
    }
    
    /**
     * 获取用户未读通知数量
     */
    public int getUnreadNotificationCount(String userId) {
        return notificationMapper.countUnreadNotificationsByUserId(userId);
    }
    
    /**
     * 标记通知为已读
     */
    @Transactional
    public boolean markNotificationAsRead(String notificationId, String userId) {
        int result = notificationMapper.markNotificationAsRead(notificationId, userId);
        return result > 0;
    }
    
    /**
     * 标记所有通知为已读
     */
    @Transactional
    public boolean markAllNotificationsAsRead(String userId) {
        int result = notificationMapper.markAllNotificationsAsRead(userId);
        logger.info("标记所有通知为已读: userId={}, count={}", userId, result);
        return result >= 0;
    }
    
    /**
     * 删除通知
     */
    @Transactional
    public boolean deleteNotification(String notificationId, String userId) {
        int result = notificationMapper.deleteNotification(notificationId, userId);
        return result > 0;
    }
    
    /**
     * 清理用户的旧通知（保留最近的100条）
     */
    @Transactional
    public void cleanupOldNotifications(String userId) {
        try {
            int deletedCount = notificationMapper.cleanupOldNotifications(userId, 100);
            if (deletedCount > 0) {
                logger.info("清理旧通知: userId={}, deletedCount={}", userId, deletedCount);
            }
        } catch (Exception e) {
            logger.error("清理旧通知失败: userId={}, error={}", userId, e.getMessage(), e);
        }
    }
}
