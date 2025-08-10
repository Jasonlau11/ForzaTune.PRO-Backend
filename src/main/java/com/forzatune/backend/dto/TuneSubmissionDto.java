package com.forzatune.backend.dto;

import lombok.Data;

import java.util.List;

/**
 * 用于前端提交创建或更新调校数据的 DTO
 */
@Data
public class TuneSubmissionDto {

    // 包含 Tune 实体的基本信息
    private String carId;
    private String shareCode;
    private String preference;
    private String piClass;
    private Integer finalPI;
    private String drivetrain;
    private String tireCompound;
    private String raceType;
    private List<String> surfaceConditions;
    private String description;
    private Boolean isProTune;
    private Boolean isParametersPublic;
    private String screenshotUrl;
    private String ownerXboxId; // 归属Xbox ID（可选）

    // 包含详细的调校参数 - 使用Object类型支持不同游戏的JSON格式
    private Object parameters;

}
