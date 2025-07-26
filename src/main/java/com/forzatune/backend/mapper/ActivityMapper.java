package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.UserActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper {
    
    // 用户活动相关
    List<UserActivity> selectByUserId(@Param("userId") String userId, @Param("limit") Integer limit);
    
    UserActivity selectById(@Param("id") String id);
    
    int insert(UserActivity activity);
    
    int update(UserActivity activity);
    
    int deleteById(@Param("id") String id);
    
    int countByUserId(@Param("userId") String userId);
    
    int countByUserIdAndType(@Param("userId") String userId, @Param("type") String type);
    
    List<UserActivity> selectRecentActivities(@Param("limit") Integer limit);
    
    List<UserActivity> selectByType(@Param("type") String type, @Param("limit") Integer limit);
} 