package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {
    
    private String id;
    private String userId;
    private String userXboxId;
    private ActivityType type;
    private String targetId; // tuneId, commentId, etc.
    private String targetName; // tune name, comment content, etc.
    private String description;
    private String createdAt;
    
    public enum ActivityType {
        LIKE,
        FAVORITE,
        COMMENT,
        UPLOAD,
        JOIN_TEAM,
        PRO_APPLICATION
    }
} 