package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知数据访问层
 */
@Mapper
public interface NotificationMapper {
    
    /**
     * 插入通知
     */
    int insertNotification(Notification notification);
    
    /**
     * 根据用户ID获取通知列表（分页）
     */
    List<Notification> selectNotificationsByUserId(
            @Param("userId") String userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
    
    /**
     * 根据用户ID获取未读通知数量
     */
    int countUnreadNotificationsByUserId(@Param("userId") String userId);
    
    /**
     * 标记通知为已读
     */
    int markNotificationAsRead(@Param("id") String id, @Param("userId") String userId);
    
    /**
     * 标记用户所有通知为已读
     */
    int markAllNotificationsAsRead(@Param("userId") String userId);
    
    /**
     * 根据ID删除通知
     */
    int deleteNotification(@Param("id") String id, @Param("userId") String userId);
    
    /**
     * 清理用户的旧通知（保留最近的N条）
     */
    int cleanupOldNotifications(@Param("userId") String userId, @Param("keepCount") int keepCount);
    
    /**
     * 根据相关ID和类型查找通知（用于避免重复通知）
     */
    Notification selectNotificationByRelatedIdAndType(
            @Param("userId") String userId,
            @Param("relatedId") String relatedId,
            @Param("type") String type,
            @Param("senderId") String senderId
    );
}
