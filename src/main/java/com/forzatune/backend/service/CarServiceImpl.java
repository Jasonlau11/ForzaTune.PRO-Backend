package com.forzatune.backend.service;

import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.dto.CarTuneCount;
import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Car;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.mapper.CarMapper;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.vo.CarsSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private TuneMapper tuneMapper;

    @Override
    public PageDto<CarDto> getCars(CarsSearchVo searchVo) {
        // 1. 执行 COUNT 查询，获取符合条件的总记录数
        long total = carMapper.countCars(searchVo);

        // 如果总数为0，直接返回空的分页对象，避免无效查询
        if (total == 0) {
            return new PageDto<>(new ArrayList<>(), searchVo.getPage(), searchVo.getLimit(), 0);
        }

        // 2. 执行分页数据查询，获取当前页的列表
        List<Car> cars = carMapper.searchCars(searchVo);

        // 3. 批量查询车辆调校数量，避免N+1查询问题
        List<String> carIds = cars.stream()
                .map(Car::getId)
                .collect(Collectors.toList());
        
        final Map<String, Long> tuneCountMap;
        if (!carIds.isEmpty()) {
            List<CarTuneCount> tuneCounts = tuneMapper.selectTuneCountByCarIds(carIds);
            tuneCountMap = tuneCounts.stream()
                    .collect(Collectors.toMap(
                            CarTuneCount::getCarId,
                            CarTuneCount::getTuneCount
                    ));
        } else {
            tuneCountMap = new HashMap<>();
        }

        // 4. 转换为DTO并设置调校数量
        List<CarDto> carDtoList = cars.stream()
                .map(car -> {
                    CarDto carDto = CarDto.fromEntity(car);
                    carDto.setTuneCount(tuneCountMap.getOrDefault(car.getId(), 0L).intValue());
                    return carDto;
                })
                .collect(Collectors.toList());

        // 5. 创建并返回 PageDto 对象
        return new PageDto<>(carDtoList, searchVo.getPage(), searchVo.getLimit(), total);
    }

    @Override
//    @Cacheable(value = "carDetails", key = "'car-detail-' + #id")
    public CarDto getCarById(String id) {
        CarDto carDto = CarDto.fromEntity(carMapper.selectById(id));
        List<Tune> tunes = tuneMapper.selectByCarId(id);
        carDto.setTunes(tunes.stream().map(tune -> TuneDto.fromEntity(tune)).collect(Collectors.toList()));
        carDto.setTuneCount(tunes.size());
        return carDto;
    }

    @Override
    public List<String> getAllManufacturers(String gameCategory) {
        return carMapper.selectAllManufacturers(gameCategory);
    }
    
    // ========== 管理员专用方法实现 ==========
    
    @Override
    public List<CarDto> getAllCarsForAdmin(String gameCategory) {
        CarsSearchVo searchVo = new CarsSearchVo();
        searchVo.setGameCategory(gameCategory);
        searchVo.setPage(1);
        searchVo.setLimit(10000); // 获取所有车辆
        
        List<Car> cars = carMapper.searchCars(searchVo);
        return cars.stream()
                .map(CarDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public CarDto addCar(CarDto carDto) {
        Car car = new Car();
        car.setId(java.util.UUID.randomUUID().toString());
        car.setName(carDto.getName());
        car.setManufacturer(carDto.getManufacturer());
        car.setYear(carDto.getYear());
        car.setCategory(convertToCarCategory(carDto.getCategory()));
        car.setDrivetrain(convertToDrivetrain(carDto.getDrivetrain()));
        car.setPi(carDto.getPi());
        car.setGameCategory(carDto.getGameCategory());
        car.setImageUrl(carDto.getImageUrl());
        car.setCreatedAt(java.time.LocalDateTime.now());
        car.setUpdatedAt(java.time.LocalDateTime.now());
        
        carMapper.insert(car);
        return CarDto.fromEntity(car);
    }
    
    @Override
    public CarDto updateCar(CarDto carDto) {
        Car existingCar = carMapper.selectById(carDto.getId());
        if (existingCar == null) {
            throw new RuntimeException("车辆不存在: " + carDto.getId());
        }
        
        existingCar.setName(carDto.getName());
        existingCar.setManufacturer(carDto.getManufacturer());
        existingCar.setYear(carDto.getYear());
        existingCar.setCategory(convertToCarCategory(carDto.getCategory()));
        existingCar.setDrivetrain(convertToDrivetrain(carDto.getDrivetrain()));
        existingCar.setPi(carDto.getPi());
        existingCar.setGameCategory(carDto.getGameCategory());
        if (carDto.getImageUrl() != null) {
            existingCar.setImageUrl(carDto.getImageUrl());
        }
        existingCar.setUpdatedAt(java.time.LocalDateTime.now());
        
        carMapper.update(existingCar);
        return CarDto.fromEntity(existingCar);
    }
    
    @Override
    public void deleteCar(String carId) {
        Car car = carMapper.selectById(carId);
        if (car == null) {
            throw new RuntimeException("车辆不存在: " + carId);
        }
        
        // 检查是否有关联的调校
        List<Tune> tunes = tuneMapper.selectByCarId(carId);
        if (!tunes.isEmpty()) {
            throw new RuntimeException("无法删除车辆，存在关联的调校数据");
        }
        
        carMapper.deleteById(carId);
    }
    
    @Override
    public void updateCarImage(String carId, String imageUrl) {
        Car car = carMapper.selectById(carId);
        if (car == null) {
            throw new RuntimeException("车辆不存在: " + carId);
        }
        
        // 只更新图片URL，避免更新其他字段导致的类型转换问题
        carMapper.updateImageUrl(carId, imageUrl);
    }
    
    @Override
    public int batchImportCars(List<CarDto> cars) {
        int successCount = 0;
        
        for (CarDto carDto : cars) {
            try {
                // 检查是否已存在相同的车辆（根据名称、制造商、年份判断）
                CarsSearchVo searchVo = new CarsSearchVo();
                searchVo.setManufacturer(carDto.getManufacturer());
                searchVo.setSearch(carDto.getName());
                searchVo.setGameCategory(carDto.getGameCategory());
                
                List<Car> existingCars = carMapper.searchCars(searchVo);
                boolean exists = existingCars.stream()
                        .anyMatch(car -> car.getName().equals(carDto.getName()) 
                                && car.getYear().equals(carDto.getYear()));
                
                if (!exists) {
                    addCar(carDto);
                    successCount++;
                }
                
            } catch (Exception e) {
                // 记录错误但继续处理其他车辆
                System.err.println("导入车辆失败: " + carDto.getName() + " - " + e.getMessage());
            }
        }
        
        return successCount;
    }
    
    // ========== 私有辅助方法 ==========
    
    /**
     * 将字符串转换为CarCategory枚举
     */
    private Car.CarCategory convertToCarCategory(String category) {
        if (category == null || category.isEmpty()) {
            return null;
        }
        
        // 映射前端传入的字符串到枚举值
        switch (category.toLowerCase()) {
            case "sportscar":
            case "sports_cars":
                return Car.CarCategory.SPORTS_CARS;
            case "musclecar":
            case "muscle_cars":
                return Car.CarCategory.MUSCLE_CARS;
            case "supercar":
            case "supercars":
                return Car.CarCategory.SUPERCARS;
            case "classiccar":
            case "classic_cars":
                return Car.CarCategory.CLASSIC_CARS;
            case "hypercar":
            case "hypercars":
                return Car.CarCategory.HYPERCARS;
            case "tracktoy":
            case "track_toys":
                return Car.CarCategory.TRACK_TOYS;
            default:
                throw new IllegalArgumentException("未知的车辆分类: " + category);
        }
    }
    
    /**
     * 将字符串转换为Drivetrain枚举
     */
    private Car.Drivetrain convertToDrivetrain(String drivetrain) {
        if (drivetrain == null || drivetrain.isEmpty()) {
            return null;
        }
        
        switch (drivetrain.toLowerCase()) {
            case "rwd":
                return Car.Drivetrain.RWD;
            case "fwd":
                return Car.Drivetrain.FWD;
            case "awd":
                return Car.Drivetrain.AWD;
            default:
                throw new IllegalArgumentException("未知的驱动方式: " + drivetrain);
        }
    }
}