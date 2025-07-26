package com.forzatune.backend.dto;

import com.forzatune.backend.entity.Car;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.PipedReader;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 车辆数据传输对象 (Data Transfer Object)
 * 用于在API层面传输车辆信息，与数据库实体(Entity)解耦。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    private String id;
    private String name;
    private String manufacturer;
    private Integer year;
    private String category; // 将Enums转换为String以方便API传输
    private Integer pi;
    private String drivetrain; // 将Enums转换为String
    private String imageUrl;
    private Integer tuneCount;
    private List<TuneDto> tunes;


    /**
     * 静态工厂方法：从 Car 实体对象转换为 CarDto 对象。
     * 通常用于将从数据库查询出的数据转换为API响应的格式。
     *
     * @param car Car 实体对象
     * @return 转换后的 CarDto 对象
     */
    public static CarDto fromEntity(Car car) {
        if (car == null) {
            return null;
        }

        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setName(car.getName());
        dto.setManufacturer(car.getManufacturer());
        dto.setYear(car.getYear());
        dto.setPi(car.getPi());
        dto.setImageUrl(car.getImageUrl());

        // 处理 Enum 到 String 的转换
        if (car.getCategory() != null) {
            dto.setCategory(car.getCategory().name());
        }
        if (car.getDrivetrain() != null) {
            dto.setDrivetrain(car.getDrivetrain().name());
        }

        return dto;
    }

    /**
     * 实例方法：将 CarDto 对象转换为 Car 实体对象。
     * 通常用于将来自API请求的数据转换为数据库可以存储的实体格式。
     *
     * @return 转换后的 Car 实体对象
     */
    public Car toEntity() {
        Car car = new Car();
        car.setId(this.getId());
        car.setName(this.getName());
        car.setManufacturer(this.getManufacturer());
        car.setYear(this.getYear());
        car.setPi(this.getPi());
        car.setImageUrl(this.getImageUrl());

        if (this.getCategory() != null) {
            car.setCategory(Car.CarCategory.valueOf(this.getCategory()));
        }
        if (this.getDrivetrain() != null) {
            car.setDrivetrain(Car.Drivetrain.valueOf(this.getDrivetrain()));
        }

        return car;
    }
}