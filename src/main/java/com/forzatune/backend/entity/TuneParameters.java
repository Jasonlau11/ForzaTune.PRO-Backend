package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuneParameters {
    
    private String id;
    private String tuneId;
    
    // 轮胎
    private Float frontTirePressure;
    private Float rearTirePressure;
    
    // 变速箱
    private Integer transmissionSpeeds;
    private Float finalDrive;
    private Float gear1Ratio;
    private Float gear2Ratio;
    private Float gear3Ratio;
    private Float gear4Ratio;
    private Float gear5Ratio;
    private Float gear6Ratio;
    private Float gear7Ratio;
    private Float gear8Ratio;
    private Float gear9Ratio;
    
    // 校准
    private Float frontCamber;
    private Float rearCamber;
    private Float frontToe;
    private Float rearToe;
    private Float frontCaster;
    
    // 防倾杆
    private Integer frontAntiRollBar;
    private Integer rearAntiRollBar;
    
    // 弹簧
    private Float frontSprings;
    private Float rearSprings;
    private Float frontRideHeight;
    private Float rearRideHeight;
    
    // 阻尼
    private Float frontRebound;
    private Float rearRebound;
    private Float frontBump;
    private Float rearBump;
    
    // 差速器
    private DifferentialType differentialType;
    private Integer frontAcceleration;
    private Integer frontDeceleration;
    private Integer rearAcceleration;
    private Integer rearDeceleration;
    private Integer centerBalance;
    
    // 制动
    private Integer brakePressure;
    private Integer frontBrakeBalance;
    
    // 空气动力学
    private Integer frontDownforce;
    private Integer rearDownforce;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum DifferentialType {
        Stock, Street, Sport, OffRoad, Rally, Drift
    }
} 