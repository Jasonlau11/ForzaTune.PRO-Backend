package com.forzatune.backend.dto;

import lombok.Data;

/**
 * 首页统计数据传输对象
 */
@Data
public class HomeStatsDto {
    private long totalCars;
    private long totalTunes;
    private long totalUsers;
    private long totalProPlayers;
}