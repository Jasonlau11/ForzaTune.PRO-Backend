package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {

    /**
     * 根据邮箱查找用户（用于登录验证）
     */
    @Select("SELECT * FROM users WHERE email = #{email} AND is_active = 1")
    User findByEmail(@Param("email") String email);

    /**
     * 根据xboxId查找用户（用于注册时检查重复）
     */
    @Select("SELECT * FROM users WHERE xbox_id = #{xboxId} AND is_active = 1")
    User findByXboxId(@Param("xboxId") String xboxId);

    /**
     * 根据用户ID查找用户
     */
    @Select("SELECT * FROM users WHERE id = #{id} AND is_active = 1")
    User findById(@Param("id") String id);

    /**
     * 插入新用户
     */
    @Insert("INSERT INTO users (id, email, password_hash, xbox_id, is_pro_player, " +
            "total_tunes, total_likes, user_tier, is_active, created_at, updated_at, last_login, email_verified_at) " +
            "VALUES (#{id}, #{email}, #{passwordHash}, #{xboxId}, #{isProPlayer}, " +
            "#{totalTunes}, #{totalLikes}, #{userTier}, #{isActive}, #{createdAt}, #{updatedAt}, #{lastLogin}, #{emailVerifiedAt})")
    int insert(User user);

    /**
     * 更新用户信息
     */
    @Update("UPDATE users SET email = #{email}, password_hash = #{passwordHash}, xbox_id = #{xboxId}, " +
            "is_pro_player = #{isProPlayer}, total_tunes = #{totalTunes}, total_likes = #{totalLikes}, " +
            "user_tier = #{userTier}, is_active = #{isActive}, updated_at = #{updatedAt}, last_login = #{lastLogin}, " +
            "email_verified_at = #{emailVerifiedAt} " +
            "WHERE id = #{id}")
    int updateUser(User user);

}