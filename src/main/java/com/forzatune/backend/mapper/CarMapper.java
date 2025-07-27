package com.forzatune.backend.mapper;

import com.forzatune.backend.dto.CarDto;
import com.forzatune.backend.entity.Car;
import com.forzatune.backend.vo.CarsSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆数据访问层接口
 */
@Mapper // Spring Boot 与 MyBatis 集成时，建议添加此注解
public interface CarMapper {

    /**
     * 根据搜索和筛选条件，分页查询车辆列表。
     * 这个方法对应 XML 中的分页数据查询。
     *
     * @param searchVo 包含所有搜索、筛选和分页参数的对象。
     * @return 当前页的车辆实体列表。
     */
    List<Car> searchCars(CarsSearchVo searchVo);

    /**
     * 根据相同的搜索和筛选条件，统计车辆总数。
     * 这个方法对应 XML 中的总数查询，用于分页计算。
     *
     * @param searchVo 包含所有搜索和筛选参数的对象（分页参数在此处不使用）。
     * @return 符合条件的总车辆数量。
     */
    long countCars(CarsSearchVo searchVo);

    // --- 你可以保留或添加其他与车辆相关的数据库操作方法 ---

    /**
     * 根据ID查询单个车辆
     * @param id 车辆ID
     * @return 车辆实体
     */
    Car selectById(String id);

    long countTotal();
    
    /**
     * 按游戏分类统计车辆总数
     * @param gameCategory 游戏分类
     * @return 总数
     */
    long countTotalByGameCategory(@Param("gameCategory") String gameCategory);

    List<Car> selectPopularCars(@Param("limit") Integer limit);
    
    /**
     * 按游戏分类查询热门车辆
     * @param limit 限制数量
     * @param gameCategory 游戏分类
     * @return 热门车辆列表
     */
    List<Car> selectPopularCarsByGameCategory(@Param("limit") Integer limit, @Param("gameCategory") String gameCategory);

}