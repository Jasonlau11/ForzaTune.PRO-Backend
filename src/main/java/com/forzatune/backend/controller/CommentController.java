package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.entity.TuneComment;
import com.forzatune.backend.entity.TuneCommentReply;
import com.forzatune.backend.service.AuthService;
import com.forzatune.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CommentController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    private final CommentService commentService;
    private final AuthService authService;
    
    /**
     * 根据调校ID获取评论
     * 对应前端: getCommentsByTuneId(tuneId)
     */
    @GetMapping("/tune/{tuneId}")
    public ResponseEntity<List<TuneComment>> getCommentsByTuneId(@PathVariable String tuneId) {
        List<TuneComment> comments = commentService.getCommentsByTuneId(tuneId);
        return ResponseEntity.ok(comments);
    }
    
    /**
     * 根据ID获取评论详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<TuneComment> getCommentById(@PathVariable String id) {
        TuneComment comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 根据评论ID获取对应的调校ID
     * 用于通知跳转
     */
    @GetMapping("/{commentId}/tune")
    public ResponseEntity<ApiResponse<Object>> getTuneIdByCommentId(@PathVariable String commentId) {
        logger.info("🔔 获取评论对应的调校ID: {}", commentId);
        
        try {
            TuneComment comment = commentService.getCommentById(commentId);
            if (comment != null) {
                String tuneId = comment.getTuneId();
                logger.info("✅ 找到评论对应的调校ID: {} -> {}", commentId, tuneId);
                
                // 创建返回数据
                java.util.Map<String, Object> result = new java.util.HashMap<>();
                result.put("tuneId", tuneId);
                
                return ResponseEntity.ok(ApiResponse.success(result));
            } else {
                logger.warn("⚠️ 评论不存在: {}", commentId);
                return ResponseEntity.ok(ApiResponse.failure("评论不存在"));
            }
        } catch (Exception e) {
            logger.error("❌ 获取评论调校ID失败: {}, 错误: {}", commentId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("获取调校ID失败: " + e.getMessage()));
        }
    }
    
    /**
     * 添加评论
     * 对应前端: addComment(tuneId, commentData)
     */
    @PostMapping
    public ResponseEntity<?> addComment(
            @RequestBody TuneComment comment,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能评论"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            String userId = authService.validateTokenAndGetUser(actualToken) != null ? 
                authService.validateTokenAndGetUser(actualToken).getId() : null;
            
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 设置用户ID
            comment.setUserId(userId);
            
            TuneComment savedComment = commentService.addComment(comment);
            logger.info("✅ 用户 {} 添加评论成功: {}", userId, comment.getTuneId());
            return ResponseEntity.ok(ApiResponse.success(savedComment));
        } catch (Exception e) {
            logger.error("❌ 添加评论失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }
    
    /**
     * 更新评论
     */
    @PutMapping("/{id}")
    public ResponseEntity<TuneComment> updateComment(@PathVariable String id, @RequestBody TuneComment comment) {
        TuneComment updatedComment = commentService.updateComment(id, comment);
        return ResponseEntity.ok(updatedComment);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 点赞评论
     * 对应前端: updateCommentLikes(commentId)
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Object> likeComment(@PathVariable String id, @RequestParam String userId) {
        Object result = commentService.likeComment(id, userId);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 添加回复
     * 对应前端: addReply(commentId, replyData)
     */
    @PostMapping("/{commentId}/replies")
    public ResponseEntity<?> addReply(
            @PathVariable String commentId,
            @RequestBody TuneCommentReply reply,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证用户认证
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.failure("需要登录才能回复"));
            }
            
            String actualToken = token.replace("Bearer ", "");
            String userId = authService.validateTokenAndGetUser(actualToken) != null ? 
                authService.validateTokenAndGetUser(actualToken).getId() : null;
            
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.failure("登录状态已过期"));
            }
            
            // 设置用户ID
            reply.setUserId(userId);
            
            TuneCommentReply savedReply = commentService.addReply(commentId, reply);
            logger.info("✅ 用户 {} 添加回复成功: {}", userId, commentId);
            return ResponseEntity.ok(ApiResponse.success(savedReply));
        } catch (Exception e) {
            logger.error("❌ 添加回复失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }
    
    /**
     * 更新回复
     */
    @PutMapping("/replies/{id}")
    public ResponseEntity<TuneCommentReply> updateReply(@PathVariable String id, @RequestBody TuneCommentReply reply) {
        TuneCommentReply updatedReply = commentService.updateReply(id, reply);
        return ResponseEntity.ok(updatedReply);
    }
    
    /**
     * 删除回复
     */
    @DeleteMapping("/replies/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable String id) {
        commentService.deleteReply(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 点赞回复
     * 对应前端: updateReplyLikes(replyId)
     */
    @PostMapping("/replies/{id}/like")
    public ResponseEntity<Object> likeReply(@PathVariable String id, @RequestParam String userId) {
        Object result = commentService.likeReply(id, userId);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取评论统计
     */
    @GetMapping("/stats")
    public ResponseEntity<Object> getCommentStats(
            @RequestParam(required = false) String tuneId,
            @RequestParam(required = false) String userId) {
        
        Object stats = commentService.getCommentStats(tuneId, userId);
        return ResponseEntity.ok(stats);
    }
} 