package com.forzatune.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 认证响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private UserInfo user;

    /**
     * 用户基本信息
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

} 