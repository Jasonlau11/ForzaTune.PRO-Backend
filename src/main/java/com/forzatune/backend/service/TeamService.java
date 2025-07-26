//package com.forzatune.backend.service;
//
//import com.forzatune.backend.entity.Team;
//import com.forzatune.backend.entity.TeamMember;
//import com.forzatune.backend.mapper.TeamMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class TeamService {
//
//    private final TeamMapper teamMapper;
//
//    /**
//     * 获取所有车队
//     */
//    public List<Team> getAllTeams(String keyword, Boolean isPublic) {
//        return teamMapper.searchTeams(keyword, isPublic);
//    }
//
//    /**
//     * 根据ID获取车队
//     */
//    public Team getTeamById(String id) {
//        return teamMapper.selectById(id);
//    }
//
//    /**
//     * 根据名称获取车队
//     */
//    public Team getTeamByName(String name) {
//        return teamMapper.selectByName(name);
//    }
//
//    /**
//     * 创建车队
//     */
//    @Transactional
//    public Team createTeam(Team team) {
//        int result = teamMapper.insert(team);
//        if (result > 0) {
//            return teamMapper.selectById(team.getId());
//        }
//        throw new RuntimeException("Failed to create team");
//    }
//
//    /**
//     * 更新车队信息
//     */
//    @Transactional
//    public Team updateTeam(String id, Team team) {
//        team.setId(id);
//        int result = teamMapper.update(team);
//        if (result > 0) {
//            return teamMapper.selectById(id);
//        }
//        throw new RuntimeException("Failed to update team");
//    }
//
//    /**
//     * 删除车队
//     */
//    @Transactional
//    public void deleteTeam(String id) {
//        int result = teamMapper.deleteById(id);
//        if (result == 0) {
//            throw new RuntimeException("Team not found");
//        }
//    }
//
//    /**
//     * 获取车队成员
//     */
//    public List<TeamMember> getTeamMembers(String teamId) {
//        return teamMapper.selectMembersByTeamId(teamId);
//    }
//
//    /**
//     * 添加车队成员
//     */
//    @Transactional
//    public TeamMember addTeamMember(String teamId, TeamMember member) {
//        member.setTeamId(teamId);
//        int result = teamMapper.insertMember(member);
//        if (result > 0) {
//            // 更新车队成员数量
//            List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//            teamMapper.updateMemberCount(teamId, members.size());
//
//            return teamMapper.selectMemberById(member.getId());
//        }
//        throw new RuntimeException("Failed to add team member");
//    }
//
//    /**
//     * 更新成员角色
//     */
//    @Transactional
//    public TeamMember updateMemberRole(String teamId, String userId, TeamMember member) {
//        member.setTeamId(teamId);
//        member.setUserId(userId);
//        int result = teamMapper.updateMember(member);
//        if (result > 0) {
//            return teamMapper.selectMemberByTeamAndUser(teamId, userId);
//        }
//        throw new RuntimeException("Failed to update member role");
//    }
//
//    /**
//     * 移除车队成员
//     */
//    @Transactional
//    public void removeTeamMember(String teamId, String userId) {
//        int result = teamMapper.deleteMemberByTeamAndUser(teamId, userId);
//        if (result > 0) {
//            // 更新车队成员数量
//            List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//            teamMapper.updateMemberCount(teamId, members.size());
//        } else {
//            throw new RuntimeException("Member not found");
//        }
//    }
//
//    /**
//     * 申请加入车队
//     */
//    @Transactional
//    public Object applyToTeam(String teamId, String userId, String gamertag, String message) {
//        int result = teamMapper.insertApplication(teamId, userId, gamertag, message);
//        if (result > 0) {
//            return Map.of("message", "申请已提交");
//        }
//        throw new RuntimeException("Failed to submit application");
//    }
//
//    /**
//     * 处理车队申请
//     */
//    @Transactional
//    public Object processApplication(String teamId, String userId, String status, String reviewedBy, String notes) {
//        int result = teamMapper.updateApplicationStatus(teamId, userId, status, reviewedBy, notes);
//        if (result > 0) {
//            if ("APPROVED".equals(status)) {
//                // 自动添加成员
//                TeamMember member = new TeamMember();
//                member.setTeamId(teamId);
//                member.setUserId(userId);
//                member.setRole(TeamMember.TeamRole.MEMBER);
//                teamMapper.insertMember(member);
//
//                // 更新车队成员数量
//                List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//                teamMapper.updateMemberCount(teamId, members.size());
//            }
//            return Map.of("message", "申请已处理");
//        }
//        throw new RuntimeException("Failed to process application");
//    }
//
//    /**
//     * 邀请用户加入车队
//     */
//    @Transactional
//    public Object inviteToTeam(String teamId, String userId, String invitedBy, String message) {
//        // 设置过期时间为7天后
//        String expiresAt = java.time.LocalDateTime.now().plusDays(7).toString();
//
//        int result = teamMapper.insertInvitation(teamId, userId, invitedBy, message, expiresAt);
//        if (result > 0) {
//            return Map.of("message", "邀请已发送");
//        }
//        throw new RuntimeException("Failed to send invitation");
//    }
//
//    /**
//     * 处理车队邀请
//     */
//    @Transactional
//    public Object processInvitation(String teamId, String userId, String status) {
//        int result = teamMapper.updateInvitationStatus(teamId, userId, status);
//        if (result > 0) {
//            if ("ACCEPTED".equals(status)) {
//                // 自动添加成员
//                TeamMember member = new TeamMember();
//                member.setTeamId(teamId);
//                member.setUserId(userId);
//                member.setRole(TeamMember.TeamRole.MEMBER);
//                teamMapper.insertMember(member);
//
//                // 更新车队成员数量
//                List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//                teamMapper.updateMemberCount(teamId, members.size());
//            }
//            return Map.of("message", "邀请已处理");
//        }
//        throw new RuntimeException("Failed to process invitation");
//    }
//
//    /**
//     * 获取用户的申请列表
//     */
//    public List<Object> getUserApplications(String userId) {
//        return teamMapper.selectApplicationsByUserId(userId);
//    }
//
//    /**
//     * 获取用户的邀请列表
//     */
//    public List<Object> getUserInvitations(String userId) {
//        return teamMapper.selectInvitationsByUserId(userId);
//    }
//
//    /**
//     * 获取车队申请列表
//     */
//    public List<Object> getTeamApplications(String teamId) {
//        return teamMapper.selectApplicationsByTeamId(teamId);
//    }
//
//    /**
//     * 获取车队邀请列表
//     */
//    public List<Object> getTeamInvitations(String teamId) {
//        return teamMapper.selectInvitationsByTeamId(teamId);
//    }
//}