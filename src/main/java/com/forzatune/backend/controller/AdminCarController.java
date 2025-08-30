package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.service.CarService;
import com.forzatune.backend.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * 车辆管理运维接口
 * 仅供管理员使用，用于维护车辆信息
 */
@RestController
@RequestMapping("/admin/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AdminCarController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminCarController.class);
    
    private final CarService carService;
    
    @Value("${app.upload.path:uploads}")
    private String uploadPath;
    
    @Value("${app.upload.car-images:car-images}")
    private String carImagesPath;
    
    /**
     * 获取所有车辆列表（管理员视图）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarDto>>> getAllCars(
            @RequestParam(value = "gameCategory", required = false) String gameCategory) {
        
        logger.info("🔧 [Admin] 获取车辆列表 - 游戏分类: {}", gameCategory);
        
        try {
            // 验证管理员权限
            if (!isAdmin()) {
                return ResponseEntity.ok(ApiResponse.failure("权限不足"));
            }
            
            List<CarDto> cars = carService.getAllCarsForAdmin(gameCategory);
            logger.info("✅ [Admin] 成功获取 {} 辆车信息", cars.size());
            
            return ResponseEntity.ok(ApiResponse.success(cars));
            
        } catch (Exception e) {
            logger.error("❌ [Admin] 获取车辆列表失败", e);
            return ResponseEntity.ok(ApiResponse.failure("获取车辆列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 添加新车辆
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CarDto>> addCar(@RequestBody CarDto carDto) {
        logger.info("🔧 [Admin] 添加新车辆: {} {} {}", carDto.getYear(), carDto.getManufacturer(), carDto.getName());
        
        try {
            // 验证管理员权限
            if (!isAdmin()) {
                return ResponseEntity.ok(ApiResponse.failure("权限不足"));
            }
            
            CarDto savedCar = carService.addCar(carDto);
            logger.info("✅ [Admin] 成功添加车辆: {}", savedCar.getId());
            
            return ResponseEntity.ok(ApiResponse.success(savedCar));
            
        } catch (Exception e) {
            logger.error("❌ [Admin] 添加车辆失败", e);
            return ResponseEntity.ok(ApiResponse.failure("添加车辆失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新车辆信息
     */
    @PutMapping("/{carId}")
    public ResponseEntity<ApiResponse<CarDto>> updateCar(
            @PathVariable String carId, 
            @RequestBody CarDto carDto) {
        
        logger.info("🔧 [Admin] 更新车辆信息: {}", carId);
        
        try {
            // 验证管理员权限
            if (!isAdmin()) {
                return ResponseEntity.ok(ApiResponse.failure("权限不足"));
            }
            
            carDto.setId(carId);
            CarDto updatedCar = carService.updateCar(carDto);
            logger.info("✅ [Admin] 成功更新车辆: {}", carId);
            
            return ResponseEntity.ok(ApiResponse.success(updatedCar));
            
        } catch (Exception e) {
            logger.error("❌ [Admin] 更新车辆失败: {}", carId, e);
            return ResponseEntity.ok(ApiResponse.failure("更新车辆失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除车辆
     */
    @DeleteMapping("/{carId}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable String carId) {
        logger.info("🔧 [Admin] 删除车辆: {}", carId);
        
        try {
            // 验证管理员权限
            if (!isAdmin()) {
                return ResponseEntity.ok(ApiResponse.failure("权限不足"));
            }
            
            carService.deleteCar(carId);
            logger.info("✅ [Admin] 成功删除车辆: {}", carId);
            
            return ResponseEntity.ok(ApiResponse.success(null));
            
        } catch (Exception e) {
            logger.error("❌ [Admin] 删除车辆失败: {}", carId, e);
            return ResponseEntity.ok(ApiResponse.failure("删除车辆失败: " + e.getMessage()));
        }
    }
    
    /**
     * 上传车辆图片
     */
    @PostMapping("/{carId}/image")
    public ResponseEntity<ApiResponse<String>> uploadCarImage(
            @PathVariable String carId,
            @RequestParam("image") MultipartFile file) {
        
        logger.info("🔧 [Admin] 上传车辆图片: {} - 文件: {}", carId, file.getOriginalFilename());
        
        try {
            // 验证管理员权限
            if (!isAdmin()) {
                return ResponseEntity.ok(ApiResponse.failure("权限不足"));
            }
            
            // 验证文件
            if (file.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.failure("文件不能为空"));
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.ok(ApiResponse.failure("只能上传图片文件"));
            }
            
            // 创建上传目录
            Path uploadDir = Paths.get(uploadPath, carImagesPath);
            Files.createDirectories(uploadDir);
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : ".jpg";
            String filename = carId + "_" + System.currentTimeMillis() + extension;
            
            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 生成访问URL
            String imageUrl = "/api/files/car-images/" + filename;
            
            // 更新车辆图片URL
            carService.updateCarImage(carId, imageUrl);
            
            logger.info("✅ [Admin] 成功上传车辆图片: {} -> {}", carId, imageUrl);
            
            return ResponseEntity.ok(ApiResponse.success(imageUrl));
            
        } catch (Exception e) {
            logger.error("❌ [Admin] 上传车辆图片失败: {}", carId, e);
            return ResponseEntity.ok(ApiResponse.failure("上传图片失败: " + e.getMessage()));
        }
    }
    
    /**
     * 批量导入车辆
     */
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<String>> batchImportCars(@RequestBody List<CarDto> cars) {
        logger.info("🔧 [Admin] 批量导入车辆: {} 辆", cars.size());
        
        try {
            // 验证管理员权限
            if (!isAdmin()) {
                return ResponseEntity.ok(ApiResponse.failure("权限不足"));
            }
            
            int successCount = carService.batchImportCars(cars);
            String message = String.format("成功导入 %d 辆车辆", successCount);
            
            logger.info("✅ [Admin] {}", message);
            return ResponseEntity.ok(ApiResponse.success(message));
            
        } catch (Exception e) {
            logger.error("❌ [Admin] 批量导入车辆失败", e);
            return ResponseEntity.ok(ApiResponse.failure("批量导入失败: " + e.getMessage()));
        }
    }
    
    /**
     * 验证管理员权限
     * TODO: 实现真正的管理员权限验证
     */
    private boolean isAdmin() {
        try {
            String userId = RequestUtils.getCurrentUserId();
            // 这里应该查询用户是否为管理员
            // 暂时返回true，后续需要实现真正的权限验证
            return userId != null;
        } catch (Exception e) {
            return false;
        }
    }
}
