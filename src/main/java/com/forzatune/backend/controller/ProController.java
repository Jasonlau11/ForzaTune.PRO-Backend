//package com.forzatune.backend.controller;
//
//import com.forzatune.backend.entity.ProCertification;
//import com.forzatune.backend.mapper.ProMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/pro")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
//public class ProController {
//
//    private final ProMapper proMapper;
//
//    /**
//     * 获取用户的PRO认证信息
//     */
//    @GetMapping("/certifications/{userId}")
//    public ResponseEntity<List<ProCertification>> getUserCertifications(@PathVariable String userId) {
//        List<ProCertification> certifications = proMapper.selectByUserId(userId);
//        return ResponseEntity.ok(certifications);
//    }
//
//    /**
//     * 添加PRO认证
//     */
//    @PostMapping("/certifications")
//    public ResponseEntity<ProCertification> addCertification(@RequestBody ProCertification certification) {
//        int result = proMapper.insertCertification(certification);
//        if (result > 0) {
//            ProCertification savedCertification = proMapper.selectById(certification.getId());
//            return ResponseEntity.ok(savedCertification);
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
//    /**
//     * 更新PRO认证状态
//     */
//    @PutMapping("/certifications/{id}")
//    public ResponseEntity<ProCertification> updateCertification(
//            @PathVariable String id,
//            @RequestBody ProCertification certification) {
//        certification.setId(id);
//        int result = proMapper.updateCertification(certification);
//        if (result > 0) {
//            ProCertification updatedCertification = proMapper.selectById(id);
//            return ResponseEntity.ok(updatedCertification);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 删除PRO认证
//     */
//    @DeleteMapping("/certifications/{id}")
//    public ResponseEntity<Void> deleteCertification(@PathVariable String id) {
//        int result = proMapper.deleteCertification(id);
//        if (result > 0) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 提交PRO申请
//     */
//    @PostMapping("/applications")
//    public ResponseEntity<Object> submitApplication(@RequestBody Map<String, Object> application) {
//        int result = proMapper.insertApplication(application);
//        if (result > 0) {
//            return ResponseEntity.ok(Map.of("message", "申请已提交"));
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
//    /**
//     * 获取PRO申请列表
//     */
//    @GetMapping("/applications")
//    public ResponseEntity<List<Object>> getApplications(
//            @RequestParam(required = false) String status) {
//        List<Object> applications = proMapper.selectApplications(status);
//        return ResponseEntity.ok(applications);
//    }
//
//    /**
//     * 获取用户的PRO申请
//     */
//    @GetMapping("/applications/user/{userId}")
//    public ResponseEntity<List<Object>> getUserApplications(@PathVariable String userId) {
//        List<Object> applications = proMapper.selectByUserId(userId);
//        return ResponseEntity.ok(applications);
//    }
//
//    /**
//     * 处理PRO申请
//     */
//    @PutMapping("/applications/{id}")
//    public ResponseEntity<Object> processApplication(
//            @PathVariable String id,
//            @RequestParam String status,
//            @RequestParam String reviewedBy,
//            @RequestParam(required = false) String notes) {
//
//        int result = proMapper.updateApplicationStatus(id, status, reviewedBy, notes);
//        if (result > 0) {
//            if ("APPROVED".equals(status)) {
//                // 自动提升用户为PRO
//                Map<String, Object> application = proMapper.selectApplicationById(id);
//                String userId = (String) application.get("userId");
//                proMapper.promoteUserToPro(userId);
//            }
//            return ResponseEntity.ok(Map.of("message", "申请已处理"));
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * 获取PRO用户统计
//     */
//    @GetMapping("/stats")
//    public ResponseEntity<Object> getProStats() {
//        int totalProUsers = proMapper.countProUsers();
//        int pendingApplications = proMapper.countPendingApplications();
//        int totalCertifications = proMapper.countTotalCertifications();
//
//        Map<String, Object> stats = Map.of(
//            "totalProUsers", totalProUsers,
//            "pendingApplications", pendingApplications,
//            "totalCertifications", totalCertifications
//        );
//
//        return ResponseEntity.ok(stats);
//    }
//}