package com.forzatune.backend.dto;

import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.entity.TuneParameters;
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
    private Tune.TunePreference preference;
    private Tune.PIClass piClass;
    private Integer finalPI;
    private Tune.Drivetrain drivetrain;
    private Tune.TireCompound tireCompound;
    private Tune.RaceType raceType;
    private List<Tune.SurfaceCondition> surfaceConditions;
    private String description;
    private Boolean isProTune;
    private Boolean isParametersPublic;
    private String screenshotUrl;

    // 包含详细的调校参数
    private TuneParametersDto parameters;

}
