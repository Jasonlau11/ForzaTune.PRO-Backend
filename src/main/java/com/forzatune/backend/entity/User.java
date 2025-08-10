package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    
    private String id;
    private String xboxId; // Xbox Live ID，用于显示和社交
    private String xuid; // Xbox 用户XUID（可空）
    private String xboxVerificationStatus; // pending/approved/denied
    private Object xboxEvidence; // JSON 证据（URL数组、备注）
    private LocalDateTime xboxVerifiedAt; // 审核通过时间
    private String xboxDeniedReason; // 审核拒绝原因
    private String email;
    private String passwordHash; // 密码哈希字段
    private Boolean isProPlayer = false;
    private LocalDateTime proPlayerSince;
    private Integer totalTunes = 0;
    private Integer totalLikes = 0;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String bio;
    private UserTier userTier = UserTier.STANDARD;
    private LocalDateTime lastLogin;
    private Boolean isActive = true;
    private LocalDateTime emailVerifiedAt;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public String getPassword() {
        return passwordHash;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }
    
    @Override
    public boolean isEnabled() {
        return isActive;
    }
    
    public enum UserTier {
        STANDARD, VERIFIED, PRO
    }
} 