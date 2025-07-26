package com.forzatune.backend.dto;

import com.forzatune.backend.entity.UserFavorite;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户收藏数据传输对象 (DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteDto {

    private String id;
    private String userId;
    private String tuneId;
    private LocalDateTime createdAt;

    /**
     * 静态工厂方法：从 UserFavorite 实体转换为 DTO。
     * @param entity 数据库实体对象
     * @return 转换后的 DTO 对象
     */
    public static UserFavoriteDto fromEntity(UserFavorite entity) {
        if (entity == null) {
            return null;
        }
        return new UserFavoriteDto(
            entity.getId(),
            entity.getUserId(),
            entity.getTuneId(),
            entity.getCreatedAt()
        );
    }

    /**
     * 实例方法：将 DTO 转换回 UserFavorite 实体。
     * @return 转换后的实体对象
     */
    public UserFavorite toEntity() {
        UserFavorite entity = new UserFavorite();
        entity.setId(this.id);
        entity.setUserId(this.userId);
        entity.setTuneId(this.tuneId);
        entity.setCreatedAt(this.createdAt);
        return entity;
    }
}
