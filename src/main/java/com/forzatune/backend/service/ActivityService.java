//package com.forzatune.backend.service;
//
//import com.forzatune.backend.entity.UserActivity;
//import com.forzatune.backend.mapper.ActivityMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ActivityService {
//
//    private final ActivityMapper activityMapper;
//
//    /**
//     * 获取用户活动列表
//     */
//    public List<UserActivity> getUserActivities(String userId, Integer limit) {
//        return activityMapper.selectByUserId(userId, limit);
//    }
//
//    /**
//     * 添加用户活动
//     */
//    @Transactional
//    public UserActivity addActivity(UserActivity activity) {
//        int result = activityMapper.insert(activity);
//        if (result > 0) {
//            return activityMapper.selectById(activity.getId());
//        }
//        throw new RuntimeException("Failed to add activity");
//    }
//
//    /**
//     * 获取用户活动统计
//     */
//    public Object getUserActivityStats(String userId) {
//        int totalActivities = activityMapper.countByUserId(userId);
//        int likedTunes = activityMapper.countByUserIdAndType(userId, "LIKED_TUNE");
//        int favoritedTunes = activityMapper.countByUserIdAndType(userId, "FAVORITED_TUNE");
//        int commentedTunes = activityMapper.countByUserIdAndType(userId, "COMMENTED_TUNE");
//        int uploadedTunes = activityMapper.countByUserIdAndType(userId, "UPLOADED_TUNE");
//
//        return Map.of(
//            "totalActivities", totalActivities,
//            "likedTunes", likedTunes,
//            "favoritedTunes", favoritedTunes,
//            "commentedTunes", commentedTunes,
//            "uploadedTunes", uploadedTunes
//        );
//    }
//
//    /**
//     * 获取最近活动
//     */
//    public List<UserActivity> getRecentActivities(Integer limit) {
//        return activityMapper.selectRecentActivities(limit);
//    }
//
//    /**
//     * 根据类型获取活动
//     */
//    public List<UserActivity> getActivitiesByType(String type, Integer limit) {
//        return activityMapper.selectByType(type, limit);
//    }
//}