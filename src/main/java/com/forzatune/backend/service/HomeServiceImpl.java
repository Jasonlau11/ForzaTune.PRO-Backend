package com.forzatune.backend.service;

import com.forzatune.backend.dto.*;
import com.forzatune.backend.mapper.CarMapper;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    @Cacheable("homeDashboard") // <-- 只需添加这一行
    public HomeDataDto getHomeDashboardData() {
        // 1. 创建最终要返回的 DTO 对象
        HomeDataDto homeData = new HomeDataDto();

        // 2. 获取热门车辆 (已优化)
        // 我们假设 CarMapper 中有一个新方法可以直接查出热门车辆的 DTO 列表
        // 这个查询会在数据库层面完成排序和计数，避免了 N+1 问题
        List<CarDto> popularCars = carMapper.selectPopularCars(4).stream().map(CarDto::fromEntity).collect(Collectors.toList()); // 传入需要的数量
        homeData.setPopularCars(popularCars);

        // 3. 获取最新调校和PRO调校 (已优化)
        // 假设 TuneMapper 中的方法已经通过 JOIN 一次性查出了所需信息
        List<TuneDto> recentTunes = tuneMapper.selectRecentTunesWithDetails(3);
        homeData.setRecentTunes(recentTunes);

        List<TuneDto> proTunes = tuneMapper.selectProTunesWithDetails(3);
        homeData.setProTunes(proTunes);

        // 4. 获取并组装统计数据 (补全缺失功能)
        HomeStatsDto stats = new HomeStatsDto();
        stats.setTotalCars(carMapper.countTotal());
        stats.setTotalTunes(tuneMapper.countTotal());
        stats.setTotalUsers(userMapper.countTotal());
        stats.setTotalProPlayers(userMapper.countProPlayers());
        homeData.setStats(stats);

        // 5. 返回组装好的完整 DTO
        return homeData;
    }
}