package com.forzatune.backend.service;

import com.forzatune.backend.entity.TuneComment;
import com.forzatune.backend.entity.TuneCommentReply;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.entity.UserActivity;
import com.forzatune.backend.mapper.CommentMapper;
import com.forzatune.backend.mapper.ActivityMapper;
import com.forzatune.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentMapper commentMapper;
    private final ActivityMapper activityMapper;
    private final UserMapper userMapper;
    
    /**
     * 根据调校ID获取评论
     */
    public List<TuneComment> getCommentsByTuneId(String tuneId) {
        List<TuneComment> list = commentMapper.selectByTuneId(tuneId);
        if (list != null) {
            for (TuneComment c : list) {
                try {
                    User u = userMapper.findById(c.getUserId());
                    if (u != null) {
                        c.setUserXboxId(u.getXboxId());
                        if (u.getIsProPlayer() != null) {
                            c.setIsProPlayer(u.getIsProPlayer());
                        }
                    }
                } catch (Exception ignore) {}
            }
        }
        return list;
    }
    
    /**
     * 根据ID获取评论
     */
    public TuneComment getCommentById(String id) {
        TuneComment c = commentMapper.selectById(id);
        if (c != null) {
            try {
                User u = userMapper.findById(c.getUserId());
                if (u != null) {
                    c.setUserXboxId(u.getXboxId());
                    if (u.getIsProPlayer() != null) {
                        c.setIsProPlayer(u.getIsProPlayer());
                    }
                }
            } catch (Exception ignore) {}
        }
        return c;
    }
    
    /**
     * 添加评论
     */
    @Transactional
    public TuneComment addComment(TuneComment comment) {
        if (comment.getId() == null || comment.getId().isEmpty()) {
            comment.setId(UUID.randomUUID().toString());
        }
        // 仅查询一次用户，后续复用
        User me = null;
        try {
            me = userMapper.findById(comment.getUserId());
            if (me != null) {
                comment.setUserXboxId(me.getXboxId());
            }
        } catch (Exception ignore) {}
        int result = commentMapper.insert(comment);
        if (result > 0) {
            TuneComment saved = commentMapper.selectById(comment.getId());
            // 运行时回填 isProPlayer
            if (me != null && me.getIsProPlayer() != null) {
                saved.setIsProPlayer(me.getIsProPlayer());
                // 同步回填展示用Xbox ID（防止DB里不同步的情况）
                if (saved.getUserXboxId() == null) {
                    saved.setUserXboxId(me.getXboxId());
                }
            }
            // 记录评论活动
            try {
                UserActivity act = new UserActivity();
                act.setUserId(saved.getUserId());
                act.setUserXboxId(me != null ? me.getXboxId() : null);
                act.setType(UserActivity.ActivityType.COMMENT);
                act.setTargetId(saved.getTuneId());
                act.setTargetName(null);
                act.setDescription("评论调校");
                activityMapper.insert(act);
            } catch (Exception ignore) {}
            return saved;
        }
        throw new RuntimeException("Failed to add comment");
    }
    
    /**
     * 更新评论
     */
    @Transactional
    public TuneComment updateComment(String id, TuneComment comment) {
        comment.setId(id);
        int result = commentMapper.update(comment);
        if (result > 0) {
            return commentMapper.selectById(id);
        }
        throw new RuntimeException("Failed to update comment");
    }
    
    /**
     * 删除评论
     */
    @Transactional
    public void deleteComment(String id) {
        int result = commentMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("Comment not found");
        }
    }
    
    /**
     * 点赞评论
     */
    @Transactional
    public Object likeComment(String commentId, String userId) {
        boolean isLiked = commentMapper.isCommentLikedByUser(commentId, userId);
        
        if (isLiked) {
            // 取消点赞
            commentMapper.deleteCommentLike(commentId, userId);
            TuneComment comment = commentMapper.selectById(commentId);
            int newLikeCount = comment.getLikeCount() - 1;
            commentMapper.updateLikeCount(commentId, newLikeCount);
//            return Map.of("liked", false, "likeCount", newLikeCount);
        } else {
            // 添加点赞
            commentMapper.insertCommentLike(commentId, userId);
            TuneComment comment = commentMapper.selectById(commentId);
            int newLikeCount = comment.getLikeCount() + 1;
            commentMapper.updateLikeCount(commentId, newLikeCount);
//            return Map.of("liked", true, "likeCount", newLikeCount);
        }
        return null;
    }
    
    /**
     * 添加回复
     */
    @Transactional
    public TuneCommentReply addReply(String commentId, TuneCommentReply reply) {
        reply.setCommentId(commentId);
        if (reply.getId() == null || reply.getId().isEmpty()) {
            reply.setId(UUID.randomUUID().toString());
        }
        // 在插入前填充回复者的 userXboxId
        try {
            User me = userMapper.findById(reply.getUserId());
            if (me != null) {
                reply.setUserXboxId(me.getXboxId());
            }
        } catch (Exception ignore) {}
        int result = commentMapper.insertReply(reply);
        if (result > 0) {
            TuneCommentReply saved = commentMapper.selectReplyById(reply.getId());
            try {
                User me = userMapper.findById(saved.getUserId());
                if (me != null && me.getIsProPlayer() != null) {
                    saved.setIsProPlayer(me.getIsProPlayer());
                }
            } catch (Exception ignore) {}
            return saved;
        }
        throw new RuntimeException("Failed to add reply");
    }
    
    /**
     * 更新回复
     */
    @Transactional
    public TuneCommentReply updateReply(String id, TuneCommentReply reply) {
        reply.setId(id);
        int result = commentMapper.updateReply(reply);
        if (result > 0) {
            return commentMapper.selectReplyById(id);
        }
        throw new RuntimeException("Failed to update reply");
    }
    
    /**
     * 删除回复
     */
    @Transactional
    public void deleteReply(String id) {
        int result = commentMapper.deleteReplyById(id);
        if (result == 0) {
            throw new RuntimeException("Reply not found");
        }
    }
    
    /**
     * 点赞回复
     */
    @Transactional
    public Object likeReply(String replyId, String userId) {
        boolean isLiked = commentMapper.isReplyLikedByUser(replyId, userId);
        
        if (isLiked) {
            // 取消点赞
            commentMapper.deleteReplyLike(replyId, userId);
            TuneCommentReply reply = commentMapper.selectReplyById(replyId);
            int newLikeCount = reply.getLikeCount() - 1;
            commentMapper.updateReplyLikeCount(replyId, newLikeCount);
//            return Map.of("liked", false, "likeCount", newLikeCount);
            return null;
        } else {
            // 添加点赞
            commentMapper.insertReplyLike(replyId, userId);
            TuneCommentReply reply = commentMapper.selectReplyById(replyId);
            int newLikeCount = reply.getLikeCount() + 1;
            commentMapper.updateReplyLikeCount(replyId, newLikeCount);
//            return Map.of("liked", true, "likeCount", newLikeCount);
            return null;
        }
    }
    
    /**
     * 获取评论统计
     */
    public Object getCommentStats(String tuneId, String userId) {
        if (tuneId != null) {
            int count = commentMapper.countByTuneId(tuneId);
//            return Map.of("tuneId", tuneId, "commentCount", count);
            return null;
        } else if (userId != null) {
            int count = commentMapper.countByUserId(userId);
//            return Map.of("userId", userId, "commentCount", count);
            return null;
        } else {
            throw new RuntimeException("需要提供tuneId或userId参数");
        }
    }
} 