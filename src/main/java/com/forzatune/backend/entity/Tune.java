package com.forzatune.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tune {
    
    private String id;
    private String carId;
    private String authorId;
    private String authorGamertag;
    private String shareCode;
    private TunePreference preference;
    private PIClass piClass;
    private Integer finalPI;
    private Drivetrain drivetrain;
    private TireCompound tireCompound;
    private RaceType raceType;
    private List<SurfaceCondition> surfaceConditions;
    private String description;
    private Boolean isProTune = false;
    private Boolean isParametersPublic = false;
    private Boolean hasDetailedParameters = false;
    private String screenshotUrl;
    private Date createdAt;
    private Date updatedAt;
    private Integer likeCount = 0;
//    private List<LapTime> lapTimes;
    private TuneParameters tuneParameters;
    
    public enum TunePreference {
        Power, Handling, Balance
    }
    
    public enum PIClass {
        X, S2, S1, A, B, C, D
    }
    
    public enum Drivetrain {
        RWD, FWD, AWD
    }
    
    public enum TireCompound {
        Stock, Street, Sport, SemiSlick, Slick, Rally, Snow, OffRoad, Drag, Drift
    }
    
    public enum RaceType {
        Road, Dirt, CrossCountry
    }
    
    public enum SurfaceCondition {
        Dry, Wet, Snow
    }
} 