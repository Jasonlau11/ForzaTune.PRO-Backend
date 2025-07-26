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
    private String gameId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum CarCategory {
        SportsCars, MuscleCars, Supercars, ClassicCars, Hypercars, TrackToys
    }
    
    public enum Drivetrain {
        RWD, FWD, AWD
    }
} 