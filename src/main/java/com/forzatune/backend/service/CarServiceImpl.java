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
    public List<String> getAllManufacturers() {
        return carMapper.selectAllManufacturers();
    }
}