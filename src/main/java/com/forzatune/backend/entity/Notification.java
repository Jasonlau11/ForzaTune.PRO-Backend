package com.forzatune.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知实体类
 */
@Data
public class Notification {
    
    /**
     * 通知ID
     */
    private String id;
    
    /**
     * 接收通知的用户ID
     */
    private String userId;
    
    /**
     * 通知类型
     */
    private NotificationType type;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 相关对象ID（调校ID、评论ID等）
     */
    private String relatedId;
    
    /**
     * 发送通知的用户ID
     */
    private String senderId;
    
    /**
     * 发送者Xbox ID
     */
    private String senderXboxId;
    
    /**
     * 是否已读
     */
    private Boolean isRead;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 通知类型枚举
     */
    public enum NotificationType {
        TUNE_LIKE("tune_like", "调校点赞"),
        TUNE_FAVORITE("tune_favorite", "调校收藏"),
        TUNE_COMMENT("tune_comment", "调校评论"),
        COMMENT_REPLY("comment_reply", "评论回复");
        
        private final String code;
        private final String description;
        
        NotificationType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
        
        public static NotificationType fromCode(String code) {
            for (NotificationType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown notification type: " + code);
        }
    }
}
