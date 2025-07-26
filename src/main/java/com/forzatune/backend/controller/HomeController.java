package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.HomeDataDto;
import com.forzatune.backend.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class HomeController {

    /**
     * 获取首页数据
     * 包含热门车辆、最新调校、PRO调校等
     * 对应前端: Home.vue 的 onMounted
     */
    @Autowired
    private HomeService homeService;

    @GetMapping("/dashboard")
    public ApiResponse<HomeDataDto> getHomeData() {
        try {
            HomeDataDto homeData = homeService.getHomeDashboardData();
            return ApiResponse.success(homeData);
        } catch (Exception e) {
            return ApiResponse.failure("获取首页数据失败: " + e.getMessage());
        }
    }
} 