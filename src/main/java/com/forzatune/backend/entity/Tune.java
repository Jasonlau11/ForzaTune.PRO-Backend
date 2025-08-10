package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tune {
    
    private String id;
    private String carId;
    private String authorId;
    private String authorXboxId; // 作者的Xbox ID
    private String ownerUserId; // 归属者站内用户ID（可空）
    private String ownerXboxId; // 归属 Xbox ID（可空）
    private String ownershipStatus; // unverified/pending/verified
    private Date ownerVerifiedAt; // 归属被验证/认领时间
    private String shareCode;
    private String preference;
    private String piClass;
    private Integer finalPI;
    private String drivetrain;
    private String tireCompound;
    private String raceType;
    private List<String> surfaceConditions;
    private String description;
    private Boolean isProTune = false;
    private Boolean isParametersPublic = false;
    private Boolean hasDetailedParameters = false;
    private String screenshotUrl;
    private Date createdAt;
    private Date updatedAt;
    private Integer likeCount = 0;
    private String gameCategory; // 游戏分类字段
    private Object parameters; // 调校参数JSON字段，支持不同游戏格式
} 