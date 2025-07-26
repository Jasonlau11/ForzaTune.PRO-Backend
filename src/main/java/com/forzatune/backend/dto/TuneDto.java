package com.forzatune.backend.dto;

import com.forzatune.backend.entity.Tune;
import lombok.Data;
import java.time.Instant; // 使用 Instant 类型处理 UTC 时间戳
import java.util.List;

/**
 * 首页调校信息数据传输对象
 */
@Data
public class TuneDto {
    private String id;
    private String shareCode;
    private String carId;
//    private String carName;
    private String authorGamertag;
    private Boolean isProTune;
    private Tune.TunePreference preference;
    private Tune.PIClass piClass;
    private Integer finalPI;
    private Tune.Drivetrain drivetrain;
    private Tune.TireCompound tireCompound;
    private List<Tune.SurfaceCondition> surfaceConditions;
    private String description;
    private Integer likeCount;
    private Integer favoriteCount;
    private String createdAt;
    private TuneParametersDto parameters;

    /**
     * 静态工厂方法：从 Tune 实体对象及关联信息转换为 TuneDto 对象。
     * 这个方法是“只读”的，专门用于将后端数据转换为API响应格式。
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
        dto.setShareCode(tune.getShareCode());
        dto.setAuthorGamertag(tune.getAuthorGamertag());
        dto.setIsProTune(tune.getIsProTune());
        dto.setLikeCount(tune.getLikeCount() != null ? tune.getLikeCount() : 0);

        // 外部传入的车辆名称
//        dto.setCarName(carName);

        if (tune.getPreference() != null) {
            dto.setPreference(tune.getPreference());
        }
        if (tune.getPiClass() != null) {
            dto.setPiClass(tune.getPiClass());
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