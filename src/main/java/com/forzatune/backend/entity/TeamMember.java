package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {
    
    private String id;
    private String teamId;
    private String userId;
    private String gamertag;
    private TeamRole role = TeamRole.MEMBER;
    private String joinedAt;
    private List<TeamPermission> permissions;
    private MemberStats stats;
    
    public enum TeamRole {
        OWNER, ADMIN, MODERATOR, MEMBER
    }
    
    public enum TeamPermission {
        MANAGE_MEMBERS,
        MANAGE_TUNES,
        INVITE_MEMBERS,
        KICK_MEMBERS,
        EDIT_TEAM_INFO,
        MODERATE_CHAT,
        VIEW_PRIVATE_TUNES
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberStats {
        private Integer tunesShared = 0;
        private Integer downloadsReceived = 0;
        private Integer likesReceived = 0;
        private Integer contributionScore = 0;
    }
} 