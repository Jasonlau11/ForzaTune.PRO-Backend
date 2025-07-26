package com.forzatune.backend.controller;

import com.forzatune.backend.entity.User;
import com.forzatune.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {
    
    private final UserMapper userMapper;
    
    /**
     * 获取所有用户
     * 对应前端: getAllUsers()
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userMapper.selectAll();
        return ResponseEntity.ok(users);
    }
    
    /**
     * 根据ID获取用户
     * 对应前端: getUserById(userId)
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 根据Gamertag获取用户
     * 对应前端: getUserByGamertag(gamertag)
     */
    @GetMapping("/gamertag/{gamertag}")
    public ResponseEntity<User> getUserByGamertag(@PathVariable String gamertag) {
        User user = userMapper.selectByGamertag(gamertag);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 获取所有PRO玩家
     */
    @GetMapping("/pro")
    public ResponseEntity<List<User>> getProPlayers() {
        List<User> users = userMapper.selectProPlayers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * 获取PRO玩家数量
     */
    @GetMapping("/pro/count")
    public ResponseEntity<Long> getProPlayerCount() {
        Long count = userMapper.countProPlayers();
        return ResponseEntity.ok(count);
    }
    
    /**
     * 创建新用户
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        int result = userMapper.insert(user);
        if (result > 0) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        int result = userMapper.update(user);
        if (result > 0) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userMapper.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * 检查Gamertag是否存在
     */
    @GetMapping("/check/gamertag/{gamertag}")
    public ResponseEntity<Boolean> checkGamertagExists(@PathVariable String gamertag) {
        boolean exists = userMapper.existsByGamertag(gamertag);
        return ResponseEntity.ok(exists);
    }
} 