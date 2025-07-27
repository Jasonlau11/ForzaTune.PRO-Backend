package com.forzatune.backend.service;


import com.forzatune.backend.dto.HomeDataDto;

/**
 * 首页数据服务接口
 */
public interface HomeService {

    /**
     * 获取首页看板所需的所有展示数据
     *
     * @param gameCategory 游戏分类过滤参数，可为null表示不过滤
     * @return 组装好的首页核心数据对象
     */
    HomeDataDto getHomeDashboardData(String gameCategory);
}