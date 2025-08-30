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
    
    // ========== 管理员专用方法 ==========
    
    /**
     * 获取所有车辆（管理员视图）
     * @param gameCategory 游戏分类
     * @return 车辆列表
     */
    List<CarDto> getAllCarsForAdmin(String gameCategory);
    
    /**
     * 添加新车辆
     * @param carDto 车辆信息
     * @return 保存后的车辆信息
     */
    CarDto addCar(CarDto carDto);
    
    /**
     * 更新车辆信息
     * @param carDto 车辆信息
     * @return 更新后的车辆信息
     */
    CarDto updateCar(CarDto carDto);
    
    /**
     * 删除车辆
     * @param carId 车辆ID
     */
    void deleteCar(String carId);
    
    /**
     * 更新车辆图片
     * @param carId 车辆ID
     * @param imageUrl 图片URL
     */
    void updateCarImage(String carId, String imageUrl);
    
    /**
     * 批量导入车辆
     * @param cars 车辆列表
     * @return 成功导入数量
     */
    int batchImportCars(List<CarDto> cars);
}