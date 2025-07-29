package com.forzatune.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 认证响应DTO
 * 前端期望结构: { token: string, user: User }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private User user;

    /**
     * 用户基本信息
     * 前端期望结构: { id, email, xboxId, isProPlayer, hasLinkedXboxId, userTier }
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String email;
        private String xboxId; // Xbox Live ID
        private Boolean isProPlayer;
        private Boolean hasLinkedXboxId;
        private String userTier;
    }

    /**
     * 用户信息（用于AuthResponse.user字段）
     * 前端期望结构: { id, email, xboxId, isProPlayer, hasLinkedXboxId }
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String id;
        private String email;
        private String xboxId;
        private Boolean isProPlayer;
        private Boolean hasLinkedXboxId;
    }
} 