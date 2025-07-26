package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LapTime {
    
    private String id;
    private String tuneId;
    private String trackId;
    private String time; // 格式: "1:55.234"
    private String proPlayerId;
    private String videoUrl;
    private Boolean isVerified = false;
    private LocalDateTime recordedAt;
    private LocalDateTime createdAt;
} 