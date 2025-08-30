package com.forzatune.backend.controller;

import com.forzatune.backend.entity.UserActivity;
import com.forzatune.backend.mapper.ActivityMapper;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ActivityController {
    
    private final ActivityMapper activityMapper;
    private final TuneMapper tuneMapper;
    private final CommentMapper commentMapper;
    
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
    public ResponseEntity<Map<String, Object>> getUserActivityStats(@PathVariable String userId) {
        // 从实际的业务表中获取统计数据
        long likedTunes = tuneMapper.countLikedByUser(userId);
        long favoritedTunes = tuneMapper.countFavoritedByUser(userId);
        int commentedTunes = commentMapper.countByUserId(userId);
        long uploadedTunes = tuneMapper.countByAuthorId(userId);

        Map<String, Object> stats = new HashMap<String, Object>();
        stats.put("likedTunes", (int) likedTunes);
        stats.put("favoritedTunes", (int) favoritedTunes);
        stats.put("commentedTunes", commentedTunes);
        stats.put("uploadedTunes", (int) uploadedTunes);

        return ResponseEntity.ok(stats);
    }
} 