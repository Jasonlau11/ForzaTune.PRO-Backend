package com.forzatune.backend.service;

import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.vo.CarsSearchVo;

import java.util.List;

public interface CarService {
    // 方法签名现在返回 PageDto<CarDto>
    PageDto<CarDto> getCars(CarsSearchVo searchVo);

    CarDto getCarById(String id);
    
    /**
     * 获取所有制造商列表
     * @param gameCategory 游戏类型
     * @return 制造商名称列表
     */
    List<String> getAllManufacturers(String gameCategory);
}