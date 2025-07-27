package com.forzatune.backend.service;

import com.forzatune.backend.dto.*;
import com.forzatune.backend.mapper.CarMapper;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HomeService 的具体实现类
 */
@Service
public class HomeServiceImpl implements HomeService {

    private final CarMapper carMapper;
    private final TuneMapper tuneMapper;
    private final UserMapper userMapper;

    /**
     * 使用构造函数注入依赖，这是 Spring 推荐的最佳实践
     */
    @Autowired
    public HomeServiceImpl(CarMapper carMapper, TuneMapper tuneMapper, UserMapper userMapper) {
        this.carMapper = carMapper;
        this.tuneMapper = tuneMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Cacheable(value = "homeDashboard", key = "#gameCategory")
    public HomeDataDto getHomeDashboardData(String gameCategory) {
        System.out.println("HomeService.getHomeDashboardData called with gameCategory: " + gameCategory);
        
        // 1. 创建最终要返回的 DTO 对象
        HomeDataDto homeData = new HomeDataDto();

        // 2. 获取热门车辆 (支持游戏分类过滤)
        List<CarDto> popularCars;
        if (gameCategory != null) {
            // 按游戏分类查询，确保返回足够数量的数据
            popularCars = carMapper.selectPopularCarsByGameCategory(4, gameCategory).stream()
                .map(CarDto::fromEntity)
                .collect(Collectors.toList());
        } else {
            // 查询所有游戏的热门车辆
            popularCars = carMapper.selectPopularCars(4).stream()
                .map(CarDto::fromEntity)
                .collect(Collectors.toList());
        }
        homeData.setPopularCars(popularCars);

        // 3. 获取最新调校和PRO调校 (支持游戏分类过滤)
        List<TuneDto> recentTunes;
        List<TuneDto> proTunes;
        
        if (gameCategory != null) {
            // 按游戏分类查询，确保返回足够数量的数据
            recentTunes = tuneMapper.selectRecentTunesWithDetailsByGameCategory(3, gameCategory);
            proTunes = tuneMapper.selectProTunesWithDetailsByGameCategory(3, gameCategory);
        } else {
            // 查询所有游戏的调校
            recentTunes = tuneMapper.selectRecentTunesWithDetails(3);
            proTunes = tuneMapper.selectProTunesWithDetails(3);
        }
        
        homeData.setRecentTunes(recentTunes);
        homeData.setProTunes(proTunes);

        // 4. 获取并组装统计数据 (支持游戏分类过滤)
        HomeStatsDto stats = new HomeStatsDto();
        
        if (gameCategory != null) {
            // 按游戏分类统计
            stats.setTotalCars(carMapper.countTotalByGameCategory(gameCategory));
            stats.setTotalTunes(tuneMapper.countTotalByGameCategory(gameCategory));
        } else {
            // 统计所有游戏数据
            stats.setTotalCars(carMapper.countTotal());
            stats.setTotalTunes(tuneMapper.countTotal());
        }

        homeData.setStats(stats);

        // 5. 返回组装好的完整 DTO
        return homeData;
    }
}