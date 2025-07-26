//package com.forzatune.backend.service;
//
//import com.forzatune.backend.entity.User;
//import com.forzatune.backend.mapper.UserMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class UserService implements UserDetailsService {
//
//    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userMapper.selectByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
//        return user;
//    }
//
//    /**
//     * 创建用户 - 并发安全的实现
//     * 使用数据库唯一约束和异常处理来保证并发安全
//     */
//    public User createUser(String email, String password, String gamertag) {
//        // 生成用户ID
//        String userId = UUID.randomUUID().toString();
//
//        // 创建用户对象
//        User user = new User();
//        user.setId(userId);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setGamertag(gamertag);
//        user.setUserTier(User.UserTier.STANDARD);
//        user.setIsActive(true);
//        user.setCreatedAt(LocalDateTime.now().toString());
//
//        try {
//            // 直接尝试插入，依赖数据库唯一约束
//            int result = userMapper.insert(user);
//            if (result > 0) {
//                return userMapper.selectByEmail(email);
//            }
//            throw new RuntimeException("Failed to create user");
//        } catch (DuplicateKeyException e) {
//            // 处理唯一性约束违反
//            String errorMessage = "Registration failed: ";
//            if (e.getMessage().contains("email")) {
//                errorMessage += "Email already exists";
//            } else if (e.getMessage().contains("gamertag")) {
//                errorMessage += "Gamertag already exists";
//            } else if (e.getMessage().contains("xbox_id")) {
//                errorMessage += "Xbox ID already exists";
//            } else {
//                errorMessage += "User information already exists";
//            }
//            throw new RuntimeException(errorMessage);
//        }
//    }
//
//    /**
//     * 更新用户信息 - 并发安全的实现
//     */
//    public User updateUser(String userId, String gamertag, String xboxId) {
//        User user = userMapper.selectById(userId);
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        boolean needsUpdate = false;
//
//        if (gamertag != null && !gamertag.equals(user.getGamertag())) {
//            user.setGamertag(gamertag);
//            needsUpdate = true;
//        }
//
//        if (xboxId != null && !xboxId.equals(user.getXboxId())) {
//            user.setXboxId(xboxId);
//            user.setUserTier(User.UserTier.VERIFIED);
//            needsUpdate = true;
//        }
//
//        if (needsUpdate) {
//            try {
//                int result = userMapper.update(user);
//                if (result > 0) {
//                    return userMapper.selectById(userId);
//                }
//                throw new RuntimeException("Failed to update user");
//            } catch (DuplicateKeyException e) {
//                String errorMessage = "Update failed: ";
//                if (e.getMessage().contains("gamertag")) {
//                    errorMessage += "Gamertag already exists";
//                } else if (e.getMessage().contains("xbox_id")) {
//                    errorMessage += "Xbox ID already exists";
//                } else {
//                    errorMessage += "User information already exists";
//                }
//                throw new RuntimeException(errorMessage);
//            }
//        }
//
//        return user;
//    }
//
//    public User promoteToPro(String userId) {
//        User user = userMapper.selectById(userId);
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        user.setIsProPlayer(true);
//        user.setProPlayerSince(LocalDateTime.now().toString());
//        user.setUserTier(User.UserTier.PRO);
//
//        int result = userMapper.update(user);
//        if (result > 0) {
//            return userMapper.selectById(userId);
//        }
//        throw new RuntimeException("Failed to promote user to PRO");
//    }
//
//    public Optional<User> findByEmail(String email) {
//        User user = userMapper.selectByEmail(email);
//        return Optional.ofNullable(user);
//    }
//
//    public Optional<User> findById(String id) {
//        User user = userMapper.selectById(id);
//        return Optional.ofNullable(user);
//    }
//
//    public List<User> getAllProPlayers() {
//        return userMapper.selectAllProPlayers();
//    }
//
//    public long getProPlayerCount() {
//        return userMapper.countProPlayers();
//    }
//
//    public void updateLastLogin(String userId) {
//        User user = userMapper.selectById(userId);
//        if (user != null) {
//            user.setLastLogin(LocalDateTime.now());
//            userMapper.update(user);
//        }
//    }
//
//    public boolean existsByEmail(String email) {
//        return userMapper.existsByEmail(email);
//    }
//
//    public boolean existsByGamertag(String gamertag) {
//        return userMapper.existsByGamertag(gamertag);
//    }
//
//    public boolean existsByXboxId(String xboxId) {
//        return userMapper.existsByXboxId(xboxId);
//    }
//}