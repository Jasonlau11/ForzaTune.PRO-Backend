package com.forzatune.backend.vo;

import lombok.Data;

import java.util.List;

/**
 * 调校搜索条件值对象
 */
@Data
public class TunesSearchVo {
    private String carId;
    private String preference;
    private String piClass;
    private String drivetrain;
    private String tireCompound;
    private String raceType;
    private List<String> surfaceConditions;
    private Boolean proOnly;
    private String sortBy;
    private String sortOrder;

    // 分页参数
    private int page;
    private int limit;

    public TunesSearchVo() {
        this.page = 1;
        this.limit = 12;
        this.proOnly = false;
        this.sortBy = "newest";
        this.sortOrder = "desc";
    }

    /**
     * 计算数据库查询的偏移量
     * @return offset
     */
    public int getOffset() {
        return (this.page - 1) < 0 ? 0 : (this.page - 1) * this.limit;
    }
} 