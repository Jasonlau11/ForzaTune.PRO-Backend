package com.forzatune.backend.controller;

import com.forzatune.backend.entity.ProCertification;
import com.forzatune.backend.mapper.ProMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import com.forzatune.backend.utils.RequestUtils;

@RestController
@RequestMapping("/pro")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ProController {

    private final ProMapper proMapper;

    /**
     * 获取用户的PRO认证信息
     */
    @GetMapping("/certifications/{userId}")
    public ResponseEntity<List<ProCertification>> getUserCertifications(@PathVariable String userId) {
        List<ProCertification> certifications = proMapper.selectCertificationsByUserId(userId);
        return ResponseEntity.ok(certifications);
    }

    /**
     * 添加PRO认证
     */
    @PostMapping("/certifications")
    public ResponseEntity<ProCertification> addCertification(@RequestBody ProCertification certification) {
        int result = proMapper.insertCertification(certification);
        if (result > 0) {
            ProCertification savedCertification = proMapper.selectById(certification.getId());
            return ResponseEntity.ok(savedCertification);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 更新PRO认证状态
     */
    @PutMapping("/certifications/{id}")
    public ResponseEntity<ProCertification> updateCertification(
            @PathVariable String id,
            @RequestBody ProCertification certification) {
        certification.setId(id);
        int result = proMapper.updateCertification(certification);
        if (result > 0) {
            ProCertification updatedCertification = proMapper.selectById(id);
            return ResponseEntity.ok(updatedCertification);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 删除PRO认证
     */
    @DeleteMapping("/certifications/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable String id) {
        int result = proMapper.deleteCertification(id);
        if (result > 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 提交PRO申请
     */
    @PostMapping("/applications")
    public ResponseEntity<Object> submitApplication(@RequestBody Map<String, Object> application) {
        // 基本参数校验与用户身份判断
        String userId = application.get("userId") != null ? application.get("userId").toString() : null;
        if (userId == null || userId.isEmpty()) {
            userId = RequestUtils.getCurrentUserId();
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "缺少用户信息，无法提交申请"));
            }
            application.put("userId", userId);
        }

        // 已是PRO则拒绝重复提交
        if (RequestUtils.getCurrentUserIsPro()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "您已是PRO玩家，无需重复申请"));
        }

        // 存在进行中或已通过的申请则拒绝重复提交
        List<Map<String, Object>> apps = proMapper.selectApplicationsByUserId(userId);
        if (apps != null) {
            for (Map<String, Object> app : apps) {
                Object st = app.get("status");
                String status = st != null ? st.toString() : "";
                if ("PENDING".equalsIgnoreCase(status) || "APPROVED".equalsIgnoreCase(status)) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "已存在进行中或已通过的申请，请勿重复提交"));
                }
            }
        }

        // 生成UUID作为主键，避免DB默认值依赖
        application.put("id", UUID.randomUUID().toString());
        int result = proMapper.insertApplication(application);
        if (result > 0) {
            return ResponseEntity.ok(Collections.singletonMap("message", "申请已提交"));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 获取PRO申请列表
     */
    @GetMapping("/applications")
    public ResponseEntity<List<Map<String, Object>>> getApplications(
            @RequestParam(required = false) String status) {
        List<Map<String, Object>> applications = proMapper.selectApplications(status);
        return ResponseEntity.ok(applications);
    }

    /**
     * 获取用户的PRO申请
     */
    @GetMapping("/applications/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserApplications(@PathVariable String userId) {
        List<Map<String, Object>> applications = proMapper.selectApplicationsByUserId(userId);
        return ResponseEntity.ok(applications);
    }

    /**
     * 处理PRO申请
     */
    @PutMapping("/applications/{id}")
    public ResponseEntity<Object> processApplication(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam String reviewedBy,
            @RequestParam(required = false) String notes) {

        int result = proMapper.updateApplicationStatus(id, status, reviewedBy, notes);
        if (result > 0) {
            if ("APPROVED".equals(status)) {
                // 自动提升用户为PRO
                Map<String, Object> application = proMapper.selectApplicationById(id);
                String userId = (String) application.get("userId");
                proMapper.promoteUserToPro(userId);
            }
            return ResponseEntity.ok(Collections.singletonMap("message", "申请已处理"));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 获取PRO用户统计
     */
    @GetMapping("/stats")
    public ResponseEntity<Object> getProStats() {
        int totalProUsers = proMapper.countProUsers();
        int pendingApplications = proMapper.countPendingApplications();
        int totalCertifications = proMapper.countTotalCertifications();

        Map<String, Object> stats = new HashMap<String, Object>();
        stats.put("totalProUsers", totalProUsers);
        stats.put("pendingApplications", pendingApplications);
        stats.put("totalCertifications", totalCertifications);

        return ResponseEntity.ok(stats);
    }
}