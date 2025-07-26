package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.ProCertification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProMapper {
    
    // PRO认证相关
//    List<ProCertification> selectByUserId(@Param("userId") String userId);
    
    ProCertification selectById(@Param("id") String id);
    
    int insertCertification(ProCertification certification);
    
    int updateCertification(ProCertification certification);
    
    int deleteCertification(@Param("id") String id);
    
    List<ProCertification> selectAllCertifications();
    
    int countByUserId(@Param("userId") String userId);
    
    // PRO申请相关
//    List<Map<String, Object>> selectApplications(@Param("status") String status);
    
//    List<Map<String, Object>> selectByUserId(@Param("userId") String userId);
    
//    Map<String, Object> selectApplicationById(@Param("id") String id);
    
    int insertApplication(Map<String, Object> application);
    
    int updateApplicationStatus(@Param("id") String id, @Param("status") String status,
                               @Param("reviewedBy") String reviewedBy, @Param("notes") String notes);
    
    // PRO用户相关
    int promoteUserToPro(@Param("userId") String userId);
    
    int countProUsers();
    
    int countPendingApplications();
    
    int countTotalCertifications();
} 