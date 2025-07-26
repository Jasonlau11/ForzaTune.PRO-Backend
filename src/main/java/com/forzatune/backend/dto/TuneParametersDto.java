package com.forzatune.backend.dto;

import com.forzatune.backend.entity.TuneParameters;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 调校参数数据传输对象 (Data Transfer Object)
 * 用于在API层面传输详细的调校参数，与数据库实体解耦。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuneParametersDto {

    // 轮胎 (存储单位: BAR, 前端可显示为 PSI, 1 BAR ≈ 14.5 PSI)
    private Float frontTirePressure;  // 1.0-3.5 BAR
    private Float rearTirePressure;   // 1.0-3.5 BAR

    // 变速箱 (无单位，比率值)
    private Integer transmissionSpeeds;  // 变速箱档位数 4-10
    private Float finalDrive;            // 最终传动比 2.0-6.0
    private Float gear1Ratio;            // 1档齿轮比 0.50-5.00
    private Float gear2Ratio;            // 2档齿轮比 0.50-5.00
    private Float gear3Ratio;            // 3档齿轮比 0.50-5.00
    private Float gear4Ratio;            // 4档齿轮比 0.50-5.00
    private Float gear5Ratio;            // 5档齿轮比 0.50-5.00
    private Float gear6Ratio;            // 6档齿轮比 0.50-5.00
    private Float gear7Ratio;            // 7档齿轮比 0.50-5.00 (可选)
    private Float gear8Ratio;            // 8档齿轮比 0.50-5.00 (可选)
    private Float gear9Ratio;            // 9档齿轮比 0.50-5.00 (可选)

    // 校准 (角度单位，无需转换)
    private Float frontCamber;           // 前轮外倾角 -5.0° - +5.0°
    private Float rearCamber;            // 后轮外倾角 -5.0° - +5.0°
    private Float frontToe;              // 前轮前束角 -2.0° - +2.0°
    private Float rearToe;               // 后轮前束角 -2.0° - +2.0°
    private Float frontCaster;           // 主销后倾角 3.0° - 9.0°

    // 防倾杆 (等级值，无需转换)
    private Integer frontAntiRollBar;    // 前防倾杆 1-65
    private Integer rearAntiRollBar;     // 后防倾杆 1-65

    // 弹簧 (存储单位: 公制, 前端可显示为英制)
    private Float frontSprings;          // 前弹簧硬度 50.0-300.0 kg/mm (前端可显示为 lbs/in, 1 kg/mm ≈ 5.71 lbs/in)
    private Float rearSprings;           // 后弹簧硬度 50.0-300.0 kg/mm (前端可显示为 lbs/in, 1 kg/mm ≈ 5.71 lbs/in)
    private Float frontRideHeight;       // 前车身高度 5.0-25.0 cm (前端可显示为 inches, 1 inch = 2.54 cm)
    private Float rearRideHeight;        // 后车身高度 5.0-25.0 cm (前端可显示为 inches, 1 inch = 2.54 cm)

    // 阻尼 (等级值，无需转换)
    private Float frontRebound;          // 前回弹阻尼 1.0-20.0
    private Float rearRebound;           // 后回弹阻尼 1.0-20.0
    private Float frontBump;             // 前压缩阻尼 1.0-20.0
    private Float rearBump;              // 后压缩阻尼 1.0-20.0

    // 差速器 (百分比值，无需转换)
    private String differentialType;     // 差速器类型: Stock/Street/Sport/OffRoad/Rally/Drift
    private Integer frontAcceleration;   // 前轮加速差速 0-100%
    private Integer frontDeceleration;   // 前轮减速差速 0-100%
    private Integer rearAcceleration;    // 后轮加速差速 0-100%
    private Integer rearDeceleration;    // 后轮减速差速 0-100%
    private Integer centerBalance;       // 中央差速平衡 0-100%

    // 制动 (百分比值，无需转换)
    private Integer brakePressure;       // 制动力 80-130%
    private Integer frontBrakeBalance;   // 制动平衡 40-60%

    // 空气动力学 (存储单位: 公制, 前端可显示为英制)
    private Integer frontDownforce;      // 前下压力 50-500 kg (前端可显示为 lbs, 1 kg ≈ 2.20 lbs)
    private Integer rearDownforce;       // 后下压力 50-500 kg (前端可显示为 lbs, 1 kg ≈ 2.20 lbs)

    /**
     * 静态工厂方法：从 TuneParameters 实体转换为 DTO。
     *
     * @param entity TuneParameters 实体对象
     * @return 转换后的 TuneParametersDto 对象
     */
    public static TuneParametersDto fromEntity(TuneParameters entity) {
        if (entity == null) {
            return null;
        }

        TuneParametersDto dto = new TuneParametersDto();

        // 轮胎
        dto.setFrontTirePressure(entity.getFrontTirePressure());
        dto.setRearTirePressure(entity.getRearTirePressure());

        // 变速箱
        dto.setTransmissionSpeeds(entity.getTransmissionSpeeds());
        dto.setFinalDrive(entity.getFinalDrive());
        dto.setGear1Ratio(entity.getGear1Ratio());
        dto.setGear2Ratio(entity.getGear2Ratio());
        dto.setGear3Ratio(entity.getGear3Ratio());
        dto.setGear4Ratio(entity.getGear4Ratio());
        dto.setGear5Ratio(entity.getGear5Ratio());
        dto.setGear6Ratio(entity.getGear6Ratio());
        dto.setGear7Ratio(entity.getGear7Ratio());
        dto.setGear8Ratio(entity.getGear8Ratio());
        dto.setGear9Ratio(entity.getGear9Ratio());

        // 校准
        dto.setFrontCamber(entity.getFrontCamber());
        dto.setRearCamber(entity.getRearCamber());
        dto.setFrontToe(entity.getFrontToe());
        dto.setRearToe(entity.getRearToe());
        dto.setFrontCaster(entity.getFrontCaster());

        // 防倾杆
        dto.setFrontAntiRollBar(entity.getFrontAntiRollBar());
        dto.setRearAntiRollBar(entity.getRearAntiRollBar());

        // 弹簧
        dto.setFrontSprings(entity.getFrontSprings());
        dto.setRearSprings(entity.getRearSprings());
        dto.setFrontRideHeight(entity.getFrontRideHeight());
        dto.setRearRideHeight(entity.getRearRideHeight());

        // 阻尼
        dto.setFrontRebound(entity.getFrontRebound());
        dto.setRearRebound(entity.getRearRebound());
        dto.setFrontBump(entity.getFrontBump());
        dto.setRearBump(entity.getRearBump());

        // 差速器
        if (entity.getDifferentialType() != null) {
            dto.setDifferentialType(entity.getDifferentialType().name());
        }
        dto.setFrontAcceleration(entity.getFrontAcceleration());
        dto.setFrontDeceleration(entity.getFrontDeceleration());
        dto.setRearAcceleration(entity.getRearAcceleration());
        dto.setRearDeceleration(entity.getRearDeceleration());
        dto.setCenterBalance(entity.getCenterBalance());

        // 制动
        dto.setBrakePressure(entity.getBrakePressure());
        dto.setFrontBrakeBalance(entity.getFrontBrakeBalance());

        // 空气动力学
        dto.setFrontDownforce(entity.getFrontDownforce());
        dto.setRearDownforce(entity.getRearDownforce());

        return dto;
    }

    /**
     * 实例方法：将 DTO 转换回 TuneParameters 实体。
     *
     * @return 转换后的 TuneParameters 实体对象
     */
    public TuneParameters toEntity() {
        TuneParameters entity = new TuneParameters();

        // 轮胎
        entity.setFrontTirePressure(this.getFrontTirePressure());
        entity.setRearTirePressure(this.getRearTirePressure());

        // 变速箱
        entity.setTransmissionSpeeds(this.getTransmissionSpeeds());
        entity.setFinalDrive(this.getFinalDrive());
        entity.setGear1Ratio(this.getGear1Ratio());
        entity.setGear2Ratio(this.getGear2Ratio());
        entity.setGear3Ratio(this.getGear3Ratio());
        entity.setGear4Ratio(this.getGear4Ratio());
        entity.setGear5Ratio(this.getGear5Ratio());
        entity.setGear6Ratio(this.getGear6Ratio());
        entity.setGear7Ratio(this.getGear7Ratio());
        entity.setGear8Ratio(this.getGear8Ratio());
        entity.setGear9Ratio(this.getGear9Ratio());

        // 校准
        entity.setFrontCamber(this.getFrontCamber());
        entity.setRearCamber(this.getRearCamber());
        entity.setFrontToe(this.getFrontToe());
        entity.setRearToe(this.getRearToe());
        entity.setFrontCaster(this.getFrontCaster());

        // 防倾杆
        entity.setFrontAntiRollBar(this.getFrontAntiRollBar());
        entity.setRearAntiRollBar(this.getRearAntiRollBar());

        // 弹簧
        entity.setFrontSprings(this.getFrontSprings());
        entity.setRearSprings(this.getRearSprings());
        entity.setFrontRideHeight(this.getFrontRideHeight());
        entity.setRearRideHeight(this.getRearRideHeight());

        // 阻尼
        entity.setFrontRebound(this.getFrontRebound());
        entity.setRearRebound(this.getRearRebound());
        entity.setFrontBump(this.getFrontBump());
        entity.setRearBump(this.getRearBump());

        // 差速器
        if (this.getDifferentialType() != null) {
            entity.setDifferentialType(TuneParameters.DifferentialType.valueOf(this.getDifferentialType()));
        }
        entity.setFrontAcceleration(this.getFrontAcceleration());
        entity.setFrontDeceleration(this.getFrontDeceleration());
        entity.setRearAcceleration(this.getRearAcceleration());
        entity.setRearDeceleration(this.getRearDeceleration());
        entity.setCenterBalance(this.getCenterBalance());

        // 制动
        entity.setBrakePressure(this.getBrakePressure());
        entity.setFrontBrakeBalance(this.getFrontBrakeBalance());

        // 空气动力学
        entity.setFrontDownforce(this.getFrontDownforce());
        entity.setRearDownforce(this.getRearDownforce());

        return entity;
    }
}