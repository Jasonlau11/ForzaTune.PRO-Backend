package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    
    private String id;
    private String name;
    private String manufacturer;
    private Integer year;
    private CarCategory category;
    private Integer pi;
    private Drivetrain drivetrain;
    private String imageUrl;
    private String gameCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum CarCategory {
        SPORTS_CARS("Sports Cars"),
        MUSCLE_CARS("Muscle Cars"), 
        SUPERCARS("Supercars"),
        CLASSIC_CARS("Classic Cars"),
        HYPERCARS("Hypercars"),
        TRACK_TOYS("Track Toys");
        
        private final String value;
        
        CarCategory(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        // 根据数据库值查找枚举
        public static CarCategory fromValue(String value) {
            for (CarCategory category : CarCategory.values()) {
                if (category.getValue().equals(value)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("Invalid CarCategory value: " + value);
        }
    }
    
    public enum Drivetrain {
        RWD, FWD, AWD
    }
} 