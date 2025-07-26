//package com.forzatune.backend.controller;
//
//import com.forzatune.backend.entity.Team;
//import com.forzatune.backend.entity.TeamMember;
//import com.forzatune.backend.mapper.TeamMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/teams")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
//public class TeamController {
//
//    private final TeamMapper teamMapper;
//
//    /**
//     * 获取所有车队
//     */
//    @GetMapping
//    public ResponseEntity<List<Team>> getAllTeams(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) Boolean isPublic) {
//
//        List<Team> teams = teamMapper.searchTeams(keyword, isPublic);
//        return ResponseEntity.ok(teams);
//    }
//
//    /**
//     * 根据ID获取车队详情
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Team> getTeamById(@PathVariable String id) {
//        Team team = teamMapper.selectById(id);
//        if (team != null) {
//            return ResponseEntity.ok(team);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 根据名称获取车队
//     */
//    @GetMapping("/name/{name}")
//    public ResponseEntity<Team> getTeamByName(@PathVariable String name) {
//        Team team = teamMapper.selectByName(name);
//        if (team != null) {
//            return ResponseEntity.ok(team);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 创建车队
//     */
//    @PostMapping
//    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
//        int result = teamMapper.insert(team);
//        if (result > 0) {
//            Team createdTeam = teamMapper.selectById(team.getId());
//            return ResponseEntity.ok(createdTeam);
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
//    /**
//     * 更新车队信息
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Team> updateTeam(@PathVariable String id, @RequestBody Team team) {
//        team.setId(id);
//        int result = teamMapper.update(team);
//        if (result > 0) {
//            Team updatedTeam = teamMapper.selectById(id);
//            return ResponseEntity.ok(updatedTeam);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 删除车队
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
//        int result = teamMapper.deleteById(id);
//        if (result > 0) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 获取车队成员
//     */
//    @GetMapping("/{teamId}/members")
//    public ResponseEntity<List<TeamMember>> getTeamMembers(@PathVariable String teamId) {
//        List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//        return ResponseEntity.ok(members);
//    }
//
//    /**
//     * 添加车队成员
//     */
//    @PostMapping("/{teamId}/members")
//    public ResponseEntity<TeamMember> addTeamMember(@PathVariable String teamId, @RequestBody TeamMember member) {
//        member.setTeamId(teamId);
//        int result = teamMapper.insertMember(member);
//        if (result > 0) {
//            // 更新车队成员数量
//            List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//            teamMapper.updateMemberCount(teamId, members.size());
//
//            TeamMember addedMember = teamMapper.selectMemberById(member.getId());
//            return ResponseEntity.ok(addedMember);
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
//    /**
//     * 更新成员角色
//     */
//    @PutMapping("/{teamId}/members/{userId}")
//    public ResponseEntity<TeamMember> updateMemberRole(
//            @PathVariable String teamId,
//            @PathVariable String userId,
//            @RequestBody TeamMember member) {
//
//        member.setTeamId(teamId);
//        member.setUserId(userId);
//        int result = teamMapper.updateMember(member);
//        if (result > 0) {
//            TeamMember updatedMember = teamMapper.selectMemberByTeamAndUser(teamId, userId);
//            return ResponseEntity.ok(updatedMember);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 移除车队成员
//     */
//    @DeleteMapping("/{teamId}/members/{userId}")
//    public ResponseEntity<Void> removeTeamMember(@PathVariable String teamId, @PathVariable String userId) {
//        int result = teamMapper.deleteMemberByTeamAndUser(teamId, userId);
//        if (result > 0) {
//            // 更新车队成员数量
//            List<TeamMember> members = teamMapper.selectMembersByTeamId(teamId);
//            teamMapper.updateMemberCount(teamId, members.size());
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 申请加入车队
//     */
//    @PostMapping("/{teamId}/apply")
//    public ResponseEntity<Object> applyToTeam(
//            @PathVariable String teamId,
//            @RequestParam String userId,
//            @RequestParam String gamertag,
//            @RequestParam(required = false) String message) {
//
//        int result = teamMapper.insertApplication(teamId, userId, gamertag, message);
//        if (result > 0) {
//            return ResponseEntity.ok(Map.of("message", "申请已提交"));
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
//    /**
//     * 处理车队申请
//     */
//    @PutMapping("/{teamId}/applications/{userId}")
//    public ResponseEntity<Object> processApplication(
//            @PathVariable String teamId,
//            @PathVariable String userId,
//            @RequestParam String status,
//            @RequestParam String reviewedBy,
//            @RequestParam(required = false) String notes) {
//
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
//            return ResponseEntity.ok(Map.of("message", "申请已处理"));
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 邀请用户加入车队
//     */
//    @PostMapping("/{teamId}/invite")
//    public ResponseEntity<Object> inviteToTeam(
//            @PathVariable String teamId,
//            @RequestParam String userId,
//            @RequestParam String invitedBy,
//            @RequestParam(required = false) String message) {
//
//        // 设置过期时间为7天后
//        String expiresAt = java.time.LocalDateTime.now().plusDays(7).toString();
//
//        int result = teamMapper.insertInvitation(teamId, userId, invitedBy, message, expiresAt);
//        if (result > 0) {
//            return ResponseEntity.ok(Map.of("message", "邀请已发送"));
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
//    /**
//     * 处理车队邀请
//     */
//    @PutMapping("/{teamId}/invitations/{userId}")
//    public ResponseEntity<Object> processInvitation(
//            @PathVariable String teamId,
//            @PathVariable String userId,
//            @RequestParam String status) {
//
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
//            return ResponseEntity.ok(Map.of("message", "邀请已处理"));
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 获取用户的申请列表
//     */
//    @GetMapping("/applications/user/{userId}")
//    public ResponseEntity<List<Object>> getUserApplications(@PathVariable String userId) {
//        List<Object> applications = teamMapper.selectApplicationsByUserId(userId);
//        return ResponseEntity.ok(applications);
//    }
//
//    /**
//     * 获取用户的邀请列表
//     */
//    @GetMapping("/invitations/user/{userId}")
//    public ResponseEntity<List<Object>> getUserInvitations(@PathVariable String userId) {
//        List<Object> invitations = teamMapper.selectInvitationsByUserId(userId);
//        return ResponseEntity.ok(invitations);
//    }
//
//    /**
//     * 获取车队申请列表
//     */
//    @GetMapping("/{teamId}/applications")
//    public ResponseEntity<List<Object>> getTeamApplications(@PathVariable String teamId) {
//        List<Object> applications = teamMapper.selectApplicationsByTeamId(teamId);
//        return ResponseEntity.ok(applications);
//    }
//
//    /**
//     * 获取车队邀请列表
//     */
//    @GetMapping("/{teamId}/invitations")
//    public ResponseEntity<List<Object>> getTeamInvitations(@PathVariable String teamId) {
//        List<Object> invitations = teamMapper.selectInvitationsByTeamId(teamId);
//        return ResponseEntity.ok(invitations);
//    }
//}