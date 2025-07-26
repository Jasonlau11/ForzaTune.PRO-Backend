package com.forzatune.backend.service;

import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.vo.CarsSearchVo;

public interface CarService {
    // 方法签名现在返回 PageDto<CarDto>
    PageDto<CarDto> getCars(CarsSearchVo searchVo);

    CarDto getCarById(String id);
}