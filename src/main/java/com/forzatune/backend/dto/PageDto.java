package com.forzatune.backend.dto;

import java.util.List;

/**
 * 通用分页数据传输对象
 * @param <T> 分页数据的类型
 */
public class PageDto<T> {

    /**
     * 当前页的数据列表
     */
    private List<T> items;

    /**
     * 当前页码 (从1开始)
     */
    private int page;

    /**
     * 每页数量
     */
    private int limit;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否有上一页
     */
    private boolean hasPrev;

    public PageDto(List<T> items, int page, int limit, long total) {
        this.items = items;
        this.page = page;
        this.limit = limit;
        this.total = total;

        // 计算总页数
        this.totalPages = (int) Math.ceil((double) total / limit);
        
        // 计算是否有下一页或上一页
        this.hasNext = page < totalPages;
        this.hasPrev = page > 1;
    }
    
    // Getters and Setters
    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
    public boolean isHasPrev() { return hasPrev; }
    public void setHasPrev(boolean hasPrev) { this.hasPrev = hasPrev; }
}