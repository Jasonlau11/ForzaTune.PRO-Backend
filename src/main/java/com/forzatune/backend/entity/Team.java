package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    
    private String id;
    private String name;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String founderId;
    private String founderGamertag;
    private String createdAt;
    private String updatedAt;
    private Integer memberCount = 0;
    private Integer maxMembers = 50;
    private Boolean isPublic = true;
    private Boolean requiresApproval = false;
    private List<String> tags;
    private TeamStats stats;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamStats {
        private Integer totalTunes = 0;
        private Integer totalDownloads = 0;
        private Integer totalLikes = 0;
        private Double averageRating = 0.0;
        private Integer activeMembersCount = 0;
    }
} 