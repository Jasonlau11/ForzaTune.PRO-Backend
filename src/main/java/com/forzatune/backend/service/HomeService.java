package com.forzatune.backend.service;


import com.forzatune.backend.dto.HomeDataDto;

/**
 * 首页数据服务接口
 */
public interface HomeService {

    /**
     * 获取首页看板所需的所有展示数据
     *
     * @return 组装好的首页核心数据对象
     */
    HomeDataDto getHomeDashboardData();
}