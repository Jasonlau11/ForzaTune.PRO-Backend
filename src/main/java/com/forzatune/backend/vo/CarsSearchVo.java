package com.forzatune.backend.vo;

import lombok.Data;

/**
 * 车辆搜索条件值对象，增加分页参数
 */
@Data
public class CarsSearchVo {
    private String search;
    private String manufacturer;
    private String category;
    private String drivetrain;
    private String gameCategory; // 游戏分类过滤参数

    // 分页参数
    private int page;
    private int limit;

    public CarsSearchVo(String search, String manufacturer, String category, String drivetrain, String gameCategory, int page, int limit) {
        this.search = search;
        this.manufacturer = manufacturer;
        this.category = category;
        this.drivetrain = drivetrain;
        this.gameCategory = gameCategory;
        this.page = page;
        this.limit = limit;
    }

    // 兼容旧版本的构造函数
    public CarsSearchVo(String search, String manufacturer, String category, String drivetrain, int page, int limit) {
        this(search, manufacturer, category, drivetrain, null, page, limit);
    }

    /**
     * 计算数据库查询的偏移量
     * @return offset
     */
    public int getOffset() {
        return (this.page - 1) < 0 ? 0 : (this.page - 1) * this.limit;
    }

    // Getters
    public String getSearch() { return search; }
    public String getManufacturer() { return manufacturer; }
    public String getCategory() { return category; }
    public String getDrivetrain() { return drivetrain; }
    public String getGameCategory() { return gameCategory; }
    public int getPage() { return page; }
    public int getLimit() { return limit; }
}