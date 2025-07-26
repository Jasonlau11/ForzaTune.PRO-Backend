package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuneComment {
    
    private String id;
    private String tuneId;
    private String userId;
    private String userGamertag;
    private Boolean isProPlayer = false;
    private String content;
    private Integer rating; // 1-5星评分
    private Integer likeCount = 0;
    private String createdAt;
    private String updatedAt;
    private List<TuneCommentReply> replies;
} 