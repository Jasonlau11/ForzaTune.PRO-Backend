package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProCertification {
    
    private String id;
    private String userId;
    private CertificationType type;
    private String title;
    private String description;
    private String verifiedAt;
    private String verifiedBy;
    private String icon;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    
    public enum CertificationType {
        CHAMPIONSHIP, WORLD_RECORD, ACHIEVEMENT, EXPERTISE
    }
} 