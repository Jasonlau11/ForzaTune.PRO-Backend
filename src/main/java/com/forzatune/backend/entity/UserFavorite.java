package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户收藏实体类，对应数据库中的 user_favorites 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavorite {

    /**
     * 收藏记录的唯一ID
     */
    private String id;

    /**
     * 用户的ID
     */
    private String userId;

    /**
     * 调校的ID
     */
    private String tuneId;

    /**
     * 收藏时间
     */
    private LocalDateTime createdAt;
}
