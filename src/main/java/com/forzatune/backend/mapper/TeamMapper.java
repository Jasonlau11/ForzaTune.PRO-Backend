package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.Team;
import com.forzatune.backend.entity.TeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamMapper {
    
    // 车队相关
    List<Team> selectAll();
    
    Team selectById(@Param("id") String id);
    
    Team selectByName(@Param("name") String name);
    
    List<Team> selectByFounderId(@Param("founderId") String founderId);
    
    List<Team> searchTeams(@Param("keyword") String keyword, @Param("isPublic") Boolean isPublic);
    
    int insert(Team team);
    
    int update(Team team);
    
    int deleteById(@Param("id") String id);
    
    int updateMemberCount(@Param("id") String id, @Param("memberCount") Integer memberCount);
    
    int updateStats(@Param("id") String id, @Param("totalTunes") Integer totalTunes, 
                   @Param("totalDownloads") Integer totalDownloads, @Param("totalLikes") Integer totalLikes,
                   @Param("averageRating") Double averageRating, @Param("activeMembersCount") Integer activeMembersCount);
    
    // 车队成员相关
    List<TeamMember> selectMembersByTeamId(@Param("teamId") String teamId);
    
    TeamMember selectMemberById(@Param("id") String id);
    
    TeamMember selectMemberByTeamAndUser(@Param("teamId") String teamId, @Param("userId") String userId);
    
    List<TeamMember> selectMembersByUserId(@Param("userId") String userId);
    
    int insertMember(TeamMember member);
    
    int updateMember(TeamMember member);
    
    int deleteMemberById(@Param("id") String id);
    
    int deleteMemberByTeamAndUser(@Param("teamId") String teamId, @Param("userId") String userId);
    
    int updateMemberStats(@Param("id") String id, @Param("tunesShared") Integer tunesShared,
                         @Param("downloadsReceived") Integer downloadsReceived, @Param("likesReceived") Integer likesReceived,
                         @Param("contributionScore") Integer contributionScore);
    
    // 车队申请相关
    List<Object> selectApplicationsByTeamId(@Param("teamId") String teamId);
    
    List<Object> selectApplicationsByUserId(@Param("userId") String userId);
    
    int insertApplication(@Param("teamId") String teamId, @Param("userId") String userId, 
                         @Param("gamertag") String gamertag, @Param("message") String message);
    
    int updateApplicationStatus(@Param("teamId") String teamId, @Param("userId") String userId,
                               @Param("status") String status, @Param("reviewedBy") String reviewedBy,
                               @Param("notes") String notes);
    
    // 车队邀请相关
    List<Object> selectInvitationsByTeamId(@Param("teamId") String teamId);
    
    List<Object> selectInvitationsByUserId(@Param("userId") String userId);
    
    int insertInvitation(@Param("teamId") String teamId, @Param("userId") String userId,
                        @Param("invitedBy") String invitedBy, @Param("message") String message,
                        @Param("expiresAt") String expiresAt);
    
    int updateInvitationStatus(@Param("teamId") String teamId, @Param("userId") String userId,
                              @Param("status") String status);
} 