package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    
    private String id;
    private String name;
    private String gameId;
    private TrackCategory category;
    private BigDecimal length; // 赛道长度（公里）
    private String location;
    private LocalDateTime createdAt;
    
    public enum TrackCategory {
        Circuit, Sprint, Drift, Drag, Rally
    }
} 