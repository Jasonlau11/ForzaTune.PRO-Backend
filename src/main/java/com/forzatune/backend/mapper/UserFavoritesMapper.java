package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收藏数据访问接口
 */
@Mapper
public interface UserFavoritesMapper {

    /**
     * 插入一条新的收藏记录
     * @param favorite 收藏实体对象
     * @return 影响的行数
     */
    int insert(UserFavorite favorite);

    /**
     * 根据用户ID和调校ID删除一条收藏记录
     * @param userId 用户ID
     * @param tuneId 调校ID
     * @return 影响的行数
     */
    int delete(@Param("userId") String userId, @Param("tuneId") String tuneId);

    /**
     * 根据用户ID和调校ID查找一条收藏记录
     * @param userId 用户ID
     * @param tuneId 调校ID
     * @return 收藏实体对象，如果不存在则返回 null
     */
    UserFavorite findByUserAndTune(@Param("userId") String userId, @Param("tuneId") String tuneId);
    
    /**
     * 根据用户ID查询其所有收藏记录
     * @param userId 用户ID
     * @return 该用户的所有收藏记录列表
     */
    List<UserFavorite> findByUserId(String userId);

}
