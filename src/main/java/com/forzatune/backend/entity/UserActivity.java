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
    private String userGamertag;
    private ActivityType type;
    private String targetId; // tuneId, commentId, etc.
    private String targetName; // tune name, comment content, etc.
    private String description;
    private String createdAt;
    
    public enum ActivityType {
        LIKED_TUNE,
        FAVORITED_TUNE,
        COMMENTED_TUNE,
        UPLOADED_TUNE,
        JOINED_TEAM,
        LEFT_TEAM,
        BECAME_PRO
    }
} 