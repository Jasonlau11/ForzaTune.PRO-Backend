package com.forzatune.backend.dto;

/**
 * @Description :
 * @Author 10331332
 * @Date 2025/07/24/下午11:00
 */

import lombok.Data;

/**
 * 首页热门车辆数据传输对象
 */
@Data // Lombok 注解，自动生成 getter, setter, toString, equals, hashCode
public class PopularCarDto {
    private String id;
    private String name;
    private String manufacturer;
    private int year;
    private String category;
    private int pi;
    private String imageUrl;
    private int tuneCount;  // 补充了接口缺失的 tuneCount 字段
    private int viewCount;
}