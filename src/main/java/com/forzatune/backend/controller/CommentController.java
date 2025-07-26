package com.forzatune.backend.controller;

import com.forzatune.backend.entity.TuneComment;
import com.forzatune.backend.entity.TuneCommentReply;
import com.forzatune.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CommentController {
    
    private final CommentService commentService;
    
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
     * 添加评论
     * 对应前端: addComment(tuneId, commentData)
     */
    @PostMapping
    public ResponseEntity<TuneComment> addComment(@RequestBody TuneComment comment) {
        TuneComment savedComment = commentService.addComment(comment);
        return ResponseEntity.ok(savedComment);
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
    public ResponseEntity<TuneCommentReply> addReply(
            @PathVariable String commentId,
            @RequestBody TuneCommentReply reply) {
        TuneCommentReply savedReply = commentService.addReply(commentId, reply);
        return ResponseEntity.ok(savedReply);
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