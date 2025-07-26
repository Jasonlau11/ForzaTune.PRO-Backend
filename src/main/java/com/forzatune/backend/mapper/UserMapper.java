package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    List<User> selectAll();
    
    User selectById(@Param("id") String id);
    
    User selectByEmail(@Param("email") String email);
    
    User selectByGamertag(@Param("gamertag") String gamertag);
    
    User selectByXboxId(@Param("xboxId") String xboxId);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(@Param("id") String id);
    
    boolean existsByEmail(@Param("email") String email);
    
    boolean existsByGamertag(@Param("gamertag") String gamertag);
    
    boolean existsByXboxId(@Param("xboxId") String xboxId);
    
    List<User> selectAllProPlayers();

    List<User> selectProPlayers();
    
    long countProPlayers();
    
    int updateLastLogin(@Param("id") String id);

    long countTotal();
}