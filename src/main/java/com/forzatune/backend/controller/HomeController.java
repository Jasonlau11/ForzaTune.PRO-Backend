package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.HomeDataDto;
import com.forzatune.backend.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * 获取首页数据
     * URL: GET /api/home/dashboard
     * 前端传参: game_category (可选)
     * 后端返回: { success: boolean, data: HomeDataDto }
     * 包含热门车辆、最新调校、PRO调校等
     * 对应前端: Home.vue 的 onMounted
     */
    @Autowired
    private HomeService homeService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<HomeDataDto>> getHomeData(@RequestParam(value = "game_category", required = false) String gameCategory) {
        logger.info("🏠 开始获取首页数据，游戏分类: {}", gameCategory);
        try {
            HomeDataDto homeData = homeService.getHomeDashboardData(gameCategory);
            logger.info("✅ 成功获取首页数据，热门车辆: {}辆, 最新调校: {}个, PRO调校: {}个", 
                homeData.getPopularCars() != null ? homeData.getPopularCars().size() : 0,
                homeData.getRecentTunes() != null ? homeData.getRecentTunes().size() : 0,
                homeData.getProTunes() != null ? homeData.getProTunes().size() : 0);
            return ResponseEntity.ok(ApiResponse.success(homeData));
        } catch (Exception e) {
            logger.error("❌ 获取首页数据失败！游戏分类: {}, 错误详情:", gameCategory, e);
            return ResponseEntity.ok(ApiResponse.failure("获取首页数据失败: " + e.getMessage()));
        }
    }
} 