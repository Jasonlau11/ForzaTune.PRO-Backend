package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.TuneComment;
import com.forzatune.backend.entity.TuneCommentReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    // 评论相关
    List<TuneComment> selectByTuneId(@Param("tuneId") String tuneId);
    
    TuneComment selectById(@Param("id") String id);
    
    int insert(TuneComment comment);
    
    int update(TuneComment comment);
    
    int deleteById(@Param("id") String id);
    
    int updateLikeCount(@Param("id") String id, @Param("likeCount") Integer likeCount);
    
    int countByTuneId(@Param("tuneId") String tuneId);
    
    int countByUserId(@Param("userId") String userId);
    
    List<String> selectTuneIdsByUserId(@Param("userId") String userId);
    
    // 回复相关
    List<TuneCommentReply> selectRepliesByCommentId(@Param("commentId") String commentId);
    
    TuneCommentReply selectReplyById(@Param("id") String id);
    
    int insertReply(TuneCommentReply reply);
    
    int updateReply(TuneCommentReply reply);
    
    int deleteReplyById(@Param("id") String id);
    
    int updateReplyLikeCount(@Param("id") String id, @Param("likeCount") Integer likeCount);
    
    // 点赞相关
    boolean isCommentLikedByUser(@Param("commentId") String commentId, @Param("userId") String userId);
    
    boolean isReplyLikedByUser(@Param("replyId") String replyId, @Param("userId") String userId);
    
    int insertCommentLike(@Param("commentId") String commentId, @Param("userId") String userId);
    
    int insertReplyLike(@Param("replyId") String replyId, @Param("userId") String userId);
    
    int deleteCommentLike(@Param("commentId") String commentId, @Param("userId") String userId);
    
    int deleteReplyLike(@Param("replyId") String replyId, @Param("userId") String userId);
} 