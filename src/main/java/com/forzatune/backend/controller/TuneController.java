package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.dto.TuneSubmissionDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.service.TuneService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tunes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TuneController {
    
    private static final Logger logger = LoggerFactory.getLogger(TuneController.class);
    
    @Autowired
    private TuneService tuneService;

    /**
     * 新增一个调校
     * URL: POST /api/tunes
     * 前端传参: TuneSubmissionDto
     * 后端返回: { success: boolean, data: Tune }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TuneDto>> createTune(@RequestBody TuneSubmissionDto tuneDto) {
        logger.info("🎵 创建调校 - 车辆: {}, 分享码: {}", tuneDto.getCarId(), tuneDto.getShareCode());
        
        try {
            // 将TuneSubmissionDto转换为TuneDto
            TuneDto tuneDtoForService = new TuneDto();
            tuneDtoForService.setCarId(tuneDto.getCarId());
            tuneDtoForService.setShareCode(tuneDto.getShareCode());
            tuneDtoForService.setPreference(tuneDto.getPreference());
            tuneDtoForService.setPiClass(tuneDto.getPiClass());
            tuneDtoForService.setFinalPI(tuneDto.getFinalPI());
            tuneDtoForService.setDrivetrain(tuneDto.getDrivetrain());
            tuneDtoForService.setTireCompound(tuneDto.getTireCompound());
            tuneDtoForService.setRaceType(tuneDto.getRaceType());
            tuneDtoForService.setSurfaceConditions(tuneDto.getSurfaceConditions());
            tuneDtoForService.setDescription(tuneDto.getDescription());
            tuneDtoForService.setIsProTune(tuneDto.getIsProTune());
            tuneDtoForService.setParameters(tuneDto.getParameters());
            
            // 记录接收到的数据用于调试
            logger.info("📋 接收到的调校数据:");
            logger.info("  - 车辆ID: {}", tuneDto.getCarId());
            logger.info("  - 分享码: {}", tuneDto.getShareCode());
            logger.info("  - 倾向: {}", tuneDto.getPreference());
            logger.info("  - PI等级: {}", tuneDto.getPiClass());
            logger.info("  - 最终PI: {}", tuneDto.getFinalPI());
            logger.info("  - 驱动: {}", tuneDto.getDrivetrain());
            logger.info("  - 轮胎: {}", tuneDto.getTireCompound());
            logger.info("  - 地面条件: {}", tuneDto.getSurfaceConditions());
            logger.info("  - 描述: {}", tuneDto.getDescription());
            logger.info("  - 是否Pro: {}", tuneDto.getIsProTune());
            logger.info("  - 参数公开: {}", tuneDto.getIsParametersPublic());
            logger.info("  - 详细参数: {}", tuneDto.getParameters() != null ? "有" : "无");
            
            TuneDto createdTune = tuneService.createTune(tuneDtoForService);
            logger.info("✅ 成功创建调校: {}", createdTune.getId());
            return ResponseEntity.ok(ApiResponse.success(createdTune));
        } catch (Exception e) {
            logger.error("❌ 创建调校失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("创建调校失败: " + e.getMessage()));
        }
    }

    /**
     * 更新一个调校
     * URL: PUT /api/tunes/{tuneId}
     * 前端传参: tuneId (路径参数), TuneSubmissionDto
     * 后端返回: { success: boolean, data: Tune }
     */
    @PutMapping("/{tuneId}")
    public ResponseEntity<ApiResponse<Tune>> updateTune(@PathVariable String tuneId, @RequestBody TuneDto tuneDto) {
        logger.info("🎵 更新调校: {}", tuneId);
        
        try {
//            Tune updatedTune = tuneService.updateTune(tuneId, tuneDto);
            logger.info("✅ 成功更新调校: {}", tuneId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            logger.error("❌ 更新调校失败: {}, 错误: {}", tuneId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("更新调校失败: " + e.getMessage()));
        }
    }

    /**
     * 删除一个调校
     * URL: DELETE /api/tunes/{tuneId}
     * 前端传参: tuneId (路径参数)
     * 后端返回: { success: boolean, data: null }
     */
    @DeleteMapping("/{tuneId}")
    public ResponseEntity<ApiResponse<Void>> deleteTune(@PathVariable String tuneId) {
        logger.info("🎵 删除调校: {}", tuneId);
        
        try {
            tuneService.deleteTune(tuneId);
            logger.info("✅ 成功删除调校: {}", tuneId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            logger.error("❌ 删除调校失败: {}, 错误: {}", tuneId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("删除调校失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取调校详情
     * URL: GET /api/tunes/{tuneId}
     * 前端传参: tuneId (路径参数)
     * 后端返回: { success: boolean, data: TuneDto }
     */
    @GetMapping("/{tuneId}")
    public ResponseEntity<ApiResponse<TuneDto>> getTuneById(@PathVariable String tuneId) {
        logger.info("🎵 获取调校详情: {}", tuneId);
        
        try {
//            TuneDto tune = tuneService.getTuneByIdWithDetail(tuneId);
//            if (tune == null) {
//                logger.warn("⚠️ 调校不存在: {}", tuneId);
//                return ResponseEntity.ok(ApiResponse.failure("调校不存在"));
//            }
            logger.info("✅ 成功获取调校详情: {}", tuneId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            logger.error("❌ 获取调校详情失败: {}, 错误: {}", tuneId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("获取调校数据失败: " + e.getMessage()));
        }
    }

    /**
     * 调校点赞/取消点赞
     * URL: POST /api/tunes/{tuneId}/like
     * 前端传参: tuneId (路径参数)
     * 后端返回: { success: boolean, data: { liked: boolean, likeCount: number } }
     */
    @PostMapping("/{tuneId}/like")
    public ResponseEntity<ApiResponse<Map<String, Object>>> likeTune(@PathVariable String tuneId) {
        logger.info("👍 点赞调校: {}", tuneId);
        
        try {
            Map<String, Object> result = tuneService.likeTune(tuneId);
            logger.info("✅ 成功点赞调校: {}, 点赞数: {}", tuneId, result.get("likeCount"));
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.error("❌ 点赞调校失败: {}, 错误: {}", tuneId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("点赞调校失败: " + e.getMessage()));
        }
    }
    
    /**
     * 调校收藏/取消收藏
     * URL: POST /api/tunes/{tuneId}/favorite
     * 前端传参: tuneId (路径参数), note (可选，收藏备注)
     * 后端返回: { success: boolean, data: { favorited: boolean, favoriteCount: number } }
     */
    @PostMapping("/{tuneId}/favorite")
    public ResponseEntity<ApiResponse<Map<String, Object>>> favoriteTune(
            @PathVariable String tuneId,
            @RequestBody(required = false) Map<String, String> requestBody) {
        
        String note = requestBody != null ? requestBody.get("note") : null;
        logger.info("⭐ 收藏调校: {}, 备注: {}", tuneId, note);
        
        try {
            Map<String, Object> result = tuneService.favoriteTune(tuneId, note);
            logger.info("✅ 成功收藏调校: {}, 收藏数: {}", tuneId, result.get("favoriteCount"));
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.error("❌ 收藏调校失败: {}, 错误: {}", tuneId, e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure("收藏调校失败: " + e.getMessage()));
        }
    }
} 