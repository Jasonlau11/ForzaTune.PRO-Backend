//package com.forzatune.backend.service;
//
//import com.forzatune.backend.entity.ProCertification;
//import com.forzatune.backend.mapper.ProMapper;
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
//public class ProService {
//
//    private final ProMapper proMapper;
//
//    /**
//     * 获取用户的PRO认证信息
//     */
//    public List<ProCertification> getUserCertifications(String userId) {
//        return proMapper.selectByUserId(userId);
//    }
//
//    /**
//     * 添加PRO认证
//     */
//    @Transactional
//    public ProCertification addCertification(ProCertification certification) {
//        int result = proMapper.insertCertification(certification);
//        if (result > 0) {
//            return proMapper.selectById(certification.getId());
//        }
//        throw new RuntimeException("Failed to add certification");
//    }
//
//    /**
//     * 更新PRO认证状态
//     */
//    @Transactional
//    public ProCertification updateCertification(String id, ProCertification certification) {
//        certification.setId(id);
//        int result = proMapper.updateCertification(certification);
//        if (result > 0) {
//            return proMapper.selectById(id);
//        }
//        throw new RuntimeException("Failed to update certification");
//    }
//
//    /**
//     * 删除PRO认证
//     */
//    @Transactional
//    public void deleteCertification(String id) {
//        int result = proMapper.deleteCertification(id);
//        if (result == 0) {
//            throw new RuntimeException("Certification not found");
//        }
//    }
//
//    /**
//     * 提交PRO申请
//     */
//    @Transactional
//    public Object submitApplication(Map<String, Object> application) {
//        int result = proMapper.insertApplication(application);
//        if (result > 0) {
//            return Map.of("message", "申请已提交");
//        }
//        throw new RuntimeException("Failed to submit application");
//    }
//
//    /**
//     * 获取PRO申请列表
//     */
//    public List<Object> getApplications(String status) {
//        return proMapper.selectApplications(status);
//    }
//
//    /**
//     * 获取用户的PRO申请
//     */
//    public List<Object> getUserApplications(String userId) {
//        return proMapper.selectByUserId(userId);
//    }
//
//    /**
//     * 处理PRO申请
//     */
//    @Transactional
//    public Object processApplication(String id, String status, String reviewedBy, String notes) {
//        int result = proMapper.updateApplicationStatus(id, status, reviewedBy, notes);
//        if (result > 0) {
//            if ("APPROVED".equals(status)) {
//                // 自动提升用户为PRO
//                Map<String, Object> application = proMapper.selectApplicationById(id);
//                String userId = (String) application.get("userId");
//                proMapper.promoteUserToPro(userId);
//            }
//            return Map.of("message", "申请已处理");
//        }
//        throw new RuntimeException("Failed to process application");
//    }
//
//    /**
//     * 获取PRO用户统计
//     */
//    public Object getProStats() {
//        int totalProUsers = proMapper.countProUsers();
//        int pendingApplications = proMapper.countPendingApplications();
//        int totalCertifications = proMapper.countTotalCertifications();
//
//        return Map.of(
//            "totalProUsers", totalProUsers,
//            "pendingApplications", pendingApplications,
//            "totalCertifications", totalCertifications
//        );
//    }
//}