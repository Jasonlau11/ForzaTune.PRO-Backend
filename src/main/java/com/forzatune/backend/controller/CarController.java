package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.service.CarService;
import com.forzatune.backend.vo.CarsSearchVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    /**
     * 获取所有车辆 - 支持搜索、筛选和分页 (优化后的版本)
     * Spring 会自动将 URL 查询参数绑定到 searchVo 对象的同名字段上。
     * * 例如，请求 URL: /api/cars?search=Supra&manufacturer=Toyota&page=2
     * Spring 会自动创建 CarsSearchVo 对象并调用:
     * setSearch("Supra"), setManufacturer("Toyota"), setPage(2)
     */
    @GetMapping
    public ApiResponse<PageDto<CarDto>> getAllCars(CarsSearchVo searchVo) {
        try {
            PageDto<CarDto> carDtoList = carService.getCars(searchVo);
            return ApiResponse.success(carDtoList);
        } catch (Exception e) {
            // 建议使用全局异常处理器来处理异常，而不是在每个Controller方法中try-catch
            return ApiResponse.failure("获取车辆数据失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取车辆详情
     * 对应前端: getCarById(carId)
     */
    @GetMapping("/{id}")
    public ApiResponse<CarDto> getCarById(@PathVariable String id) {
        try {
            return ApiResponse.success(carService.getCarById(id));
        } catch (Exception e) {
            return ApiResponse.failure("获取车辆详情失败: " + e.getMessage());
        }
    }
}
