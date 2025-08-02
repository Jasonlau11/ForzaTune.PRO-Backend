package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.service.CarService;
import com.forzatune.backend.service.TuneService;
import com.forzatune.backend.utils.RequestUtils;
import com.forzatune.backend.vo.CarsSearchVo;
import com.forzatune.backend.vo.TunesSearchVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;
    
    @Autowired
    private TuneService tuneService;

    /**
     * 获取车辆列表 - 支持搜索、筛选和分页
     * URL: GET /api/cars
     * 前端传参: 
     *   - page: 页码
     *   - limit: 每页数量
     *   - search: 搜索关键词
     *   - game_category: 游戏分类
     *   - categories: 车辆分类数组
     *   - manufacturer: 制造商
     *   - drivetrain: 驱动方式
     *   - sort_by: 排序字段
     *   - sort_order: 排序方向
     * 后端返回: { success: boolean, data: { cars: CarDto[], pagination: PageInfo } }
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageDto<CarDto>>> getAllCars(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "game_category", required = false) String gameCategory,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "manufacturer", required = false) String manufacturer,
            @RequestParam(value = "drivetrain", required = false) String drivetrain,
            @RequestParam(value = "sort_by", defaultValue = "name") String sortBy,
            @RequestParam(value = "sort_order", defaultValue = "asc") String sortOrder) {
        
        logger.info("🚗 获取车辆列表 - 页码: {}, 每页: {}, 搜索: {}, 游戏: {}, 制造商: {}, 驱动: {}", 
                   page, limit, search, gameCategory, manufacturer, drivetrain);
        
        try {
            // 构建搜索条件
            CarsSearchVo searchVo = new CarsSearchVo();
            searchVo.setPage(page);
            searchVo.setLimit(limit);
            searchVo.setSearch(search);
            searchVo.setGameCategory(gameCategory);
            searchVo.setCategories(categories);
            searchVo.setManufacturer(manufacturer);
            searchVo.setDrivetrain(drivetrain);
            searchVo.setSortBy(sortBy);
            searchVo.setSortOrder(sortOrder);
            
            PageDto<CarDto> result = carService.getCars(searchVo);
            logger.info("✅ 成功获取车辆列表，总数: {}, 当前页: {}", result.getTotal(), result.getPage());
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.error("❌ 获取车辆列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("获取车辆数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取制造商列表
     * URL: GET /api/cars/manufacturers
     * 前端传参: 无（从请求头获取游戏类型）
     * 后端返回: { success: boolean, data: string[] }
     */
    @GetMapping("/manufacturers")
    public ResponseEntity<ApiResponse<List<String>>> getManufacturers() {
        String gameCategory = RequestUtils.getCurrentGameCategory();
        logger.info("🏭 获取制造商列表 - 游戏类型: {}", gameCategory);
        
        try {
            List<String> manufacturers = carService.getAllManufacturers(gameCategory);
            logger.info("✅ 成功获取制造商列表，数量: {}, 游戏类型: {}", manufacturers.size(), gameCategory);
            return ResponseEntity.ok(ApiResponse.success(manufacturers));
        } catch (Exception e) {
            logger.error("❌ 获取制造商列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("获取制造商列表失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取车辆详情
     * URL: GET /api/cars/{carId}
     * 前端传参: carId (路径参数)
     * 后端返回: { success: boolean, data: CarDto }
     */
    @GetMapping("/{carId}")
    public ResponseEntity<ApiResponse<CarDto>> getCarById(@PathVariable String carId) {
        logger.info("🚗 获取车辆详情: {}", carId);
        
        try {
            CarDto car = carService.getCarById(carId);
            if (car == null) {
                logger.warn("⚠️ 车辆不存在: {}", carId);
                return ResponseEntity.ok(ApiResponse.failure("车辆不存在"));
            }
            logger.info("✅ 成功获取车辆详情: {}", car.getName());
            return ResponseEntity.ok(ApiResponse.success(car));
        } catch (Exception e) {
            logger.error("❌ 获取车辆详情失败: {}, 错误: {}", carId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("获取车辆详情失败: " + e.getMessage()));
        }
    }

    /**
     * 获取车辆调校列表
     * URL: GET /api/cars/{carId}/tunes
     * 前端传参: 
     *   - carId: 车辆ID (路径参数)
     *   - page: 页码
     *   - limit: 每页数量
     *   - preference: 调校偏好
     *   - pi_class: PI等级
     *   - drivetrain: 驱动方式
     *   - tire_compound: 轮胎类型
     *   - race_type: 比赛类型
     *   - surface_conditions: 地面条件数组
     *   - pro_only: 是否只看Pro调校
     *   - sort_by: 排序字段
     *   - sort_order: 排序方向
     * 后端返回: { success: boolean, data: { tunes: TuneDto[], pagination: PageInfo } }
     */
    @GetMapping("/{carId}/tunes")
    public ResponseEntity<ApiResponse<PageDto<TuneDto>>> getCarTunes(
            @PathVariable String carId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "preference", required = false) String preference,
            @RequestParam(value = "pi_class", required = false) String piClass,
            @RequestParam(value = "drivetrain", required = false) String drivetrain,
            @RequestParam(value = "tire_compound", required = false) String tireCompound,
            @RequestParam(value = "race_type", required = false) String raceType,
            @RequestParam(value = "surface_conditions", required = false) List<String> surfaceConditions,
            @RequestParam(value = "pro_only", defaultValue = "false") Boolean proOnly,
            @RequestParam(value = "sort_by", defaultValue = "newest") String sortBy,
            @RequestParam(value = "sort_order", defaultValue = "desc") String sortOrder) {
        
        logger.info("🎵 获取车辆调校列表 - 车辆: {}, 页码: {}, 每页: {}, 偏好: {}, PI等级: {}, Pro调校: {}", 
                   carId, page, limit, preference, piClass, proOnly);
        
        try {
            // 构建搜索条件
            TunesSearchVo searchVo = new TunesSearchVo();
            searchVo.setCarId(carId);
            searchVo.setPage(page);
            searchVo.setLimit(limit);
            searchVo.setPreference(preference);
            searchVo.setPiClass(piClass);
            searchVo.setDrivetrain(drivetrain);
            searchVo.setTireCompound(tireCompound);
            searchVo.setRaceType(raceType);
            searchVo.setSurfaceConditions(surfaceConditions);
            searchVo.setProOnly(proOnly);
            searchVo.setSortBy(sortBy);
            searchVo.setSortOrder(sortOrder);
            
            PageDto<TuneDto> result = tuneService.getTunesByCar(searchVo);
            logger.info("✅ 成功获取车辆调校列表，总数: {}, 当前页: {}", result.getTotal(), result.getPage());
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.error("❌ 获取车辆调校列表失败: {}, 错误: {}", carId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("获取车辆调校列表失败: " + e.getMessage()));
        }
    }
}
