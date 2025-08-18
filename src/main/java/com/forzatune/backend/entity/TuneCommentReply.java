package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuneCommentReply {
    
    private String id;
    private String commentId;
    private String userId;
    private String userXboxId;
    private Boolean isProPlayer = false;
    private String content;
    private Integer likeCount = 0;
    private String createdAt;
    private String updatedAt;
} 