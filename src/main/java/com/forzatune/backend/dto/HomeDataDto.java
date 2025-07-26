package com.forzatune.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * 首页展示的核心数据集合
 */
@Data
public class HomeDataDto {
    private List<CarDto> popularCars;
    private List<TuneDto> recentTunes;
    private List<TuneDto> proTunes;
    private HomeStatsDto stats; // 包含了缺失的 stats 对象
}