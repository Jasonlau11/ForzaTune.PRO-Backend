package com.forzatune.backend.dto;

import com.forzatune.backend.entity.Tune;
import lombok.Data;
import java.util.List;

/**
 * 首页调校信息数据传输对象
 */
@Data
public class TuneDto {
    private String id;
    private String shareCode;
    private String carId;
    private String authorId;
    private String authorXboxId; // 作者的Xbox ID
    private String ownerUserId; // 归属者用户ID（可空）
    private String ownerXboxId; // 归属Xbox ID（可空）
    private String ownershipStatus; // 归属状态
    private String ownerVerifiedAt; // 归属验证时间
    private Boolean ownerIsPro; // 归属者是否为PRO（用于展示与筛选）
    private Boolean isProTune;
    private String preference;
    private String piClass;
    private Integer finalPI;
    private String drivetrain;
    private String tireCompound;
    private String raceType;
    private List<String> surfaceConditions;
    private String description;
    private Integer likeCount;
    private Integer favoriteCount;
    private String createdAt;
    private String gameCategory; // 游戏分类字段
    private Boolean isParametersPublic; // 是否公开详细参数
    private Object parameters; // 调校参数JSON对象，支持不同游戏格式

    /**
     * 静态工厂方法：从 Tune 实体对象及关联信息转换为 TuneDto 对象。
     * 这个方法是"只读"的，专门用于将后端数据转换为API响应格式。
     *
     * @param tune 调校实体对象
     * @return 转换后的 TuneDto 对象
     */
    public static TuneDto fromEntity(Tune tune) {
        if (tune == null) {
            return null;
        }

        TuneDto dto = new TuneDto();
        dto.setId(tune.getId());
        dto.setAuthorId(tune.getAuthorId());
        dto.setAuthorXboxId(tune.getAuthorXboxId());
        dto.setShareCode(tune.getShareCode());
        dto.setCarId(tune.getCarId());
        // 归属信息
        dto.setOwnerUserId(tune.getOwnerUserId());
        dto.setOwnerXboxId(tune.getOwnerXboxId());
        dto.setOwnershipStatus(tune.getOwnershipStatus());
        if (tune.getOwnerVerifiedAt() != null) {
            dto.setOwnerVerifiedAt(String.valueOf(tune.getOwnerVerifiedAt().getTime()));
        }
        // 归属人是否为PRO
        // 简化：当 ownerUserId 存在时，调用方应补充 ownerIsPro；这里先默认由 isProTune 决定，后续在查询处修正
        dto.setOwnerIsPro(tune.getIsProTune());
        dto.setIsProTune(tune.getIsProTune());
        dto.setIsParametersPublic(tune.getIsParametersPublic());
        dto.setLikeCount(tune.getLikeCount() != null ? tune.getLikeCount() : 0);
        dto.setGameCategory(tune.getGameCategory()); // 设置游戏分类

        // 外部传入的车辆名称
//        dto.setCarName(carName);

        if (tune.getPreference() != null) {
            dto.setPreference(tune.getPreference());
        }
        if (tune.getPiClass() != null) {
            dto.setPiClass(tune.getPiClass());
        }
        if (tune.getDrivetrain() != null) {
            dto.setDrivetrain(tune.getDrivetrain());
        }
        if (tune.getTireCompound() != null) {
            dto.setTireCompound(tune.getTireCompound());
        }
        if (tune.getRaceType() != null) {
            dto.setRaceType(tune.getRaceType());
        }
        if (tune.getSurfaceConditions() != null) {
            dto.setSurfaceConditions(tune.getSurfaceConditions());
        }
        if (tune.getDescription() != null) {
            dto.setDescription(tune.getDescription());
        }
        if (tune.getFinalPI() != null) {
            dto.setFinalPI(tune.getFinalPI());
        }

        // 处理调校参数 - 直接传递JSON对象
        if (tune.getParameters() != null && tune.getIsParametersPublic()) {
            dto.setParameters(tune.getParameters());
        }

        if (tune.getCreatedAt() != null) {
            try {
                dto.setCreatedAt(tune.getCreatedAt().toString());
            } catch (Exception e) {
                // 如果格式不正确，可以设置一个默认值或记录日志
                dto.setCreatedAt(null);
            }
        }

        return dto;
    }
}