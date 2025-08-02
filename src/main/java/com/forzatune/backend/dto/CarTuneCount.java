package com.forzatune.backend.dto;

/**
 * 车辆调校数量DTO
 * 用于批量查询车辆调校数量时返回结果
 */
public class CarTuneCount {
    
    private String carId;
    private Long tuneCount;
    
    public CarTuneCount() {}
    
    public CarTuneCount(String carId, Long tuneCount) {
        this.carId = carId;
        this.tuneCount = tuneCount;
    }
    
    public String getCarId() {
        return carId;
    }
    
    public void setCarId(String carId) {
        this.carId = carId;
    }
    
    public Long getTuneCount() {
        return tuneCount;
    }
    
    public void setTuneCount(Long tuneCount) {
        this.tuneCount = tuneCount;
    }
    
    @Override
    public String toString() {
        return "CarTuneCount{" +
                "carId='" + carId + '\'' +
                ", tuneCount=" + tuneCount +
                '}';
    }
} 