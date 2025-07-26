package com.forzatune.backend.controller;

import com.forzatune.backend.entity.UserActivity;
import com.forzatune.backend.mapper.ActivityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ActivityController {
    
    private final ActivityMapper activityMapper;
    
    /**
     * 获取用户活动列表
     * 对应前端: getUserActivities(userId)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserActivity>> getUserActivities(
            @PathVariable String userId,
            @RequestParam(defaultValue = "20") Integer limit) {
        
        List<UserActivity> activities = activityMapper.selectByUserId(userId, limit);
        return ResponseEntity.ok(activities);
    }
    
    /**
     * 添加用户活动
     */
    @PostMapping
    public ResponseEntity<UserActivity> addActivity(@RequestBody UserActivity activity) {
        int result = activityMapper.insert(activity);
        if (result > 0) {
            UserActivity savedActivity = activityMapper.selectById(activity.getId());
            return ResponseEntity.ok(savedActivity);
        }
        return ResponseEntity.badRequest().build();
    }
    
    /**
     * 获取活动统计
     */
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Object> getUserActivityStats(@PathVariable String userId) {
        int totalActivities = activityMapper.countByUserId(userId);
        int likedTunes = activityMapper.countByUserIdAndType(userId, "LIKED_TUNE");
        int favoritedTunes = activityMapper.countByUserIdAndType(userId, "FAVORITED_TUNE");
        int commentedTunes = activityMapper.countByUserIdAndType(userId, "COMMENTED_TUNE");
        int uploadedTunes = activityMapper.countByUserIdAndType(userId, "UPLOADED_TUNE");
        
//        Map<String, Object> stats = Map.of(
//            "totalActivities", totalActivities,
//            "likedTunes", likedTunes,
//            "favoritedTunes", favoritedTunes,
//            "commentedTunes", commentedTunes,
//            "uploadedTunes", uploadedTunes
//        );
        
//        return ResponseEntity.ok(stats);
        return ResponseEntity.ok(null);
    }
} 