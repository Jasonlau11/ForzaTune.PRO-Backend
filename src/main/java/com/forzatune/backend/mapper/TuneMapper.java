package com.forzatune.backend.mapper;

import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TuneMapper {

    Tune selectById(@Param("id") String id);

    Tune selectByIdWithDetail(@Param("id") String id);

    void incrementLikeCount(@Param("id") String id);
    
    List<Tune> selectByCarId(@Param("carId") String carId);
    
    List<Tune> selectByAuthorId(@Param("authorId") String authorId);
    
    List<Tune> selectProTunes();
    
    // 新增方法：获取最新调校
    List<Tune> selectRecentTunes();
    
    int insert(Tune tune);
    
    int update(Tune tune);
    
    int deleteById(@Param("id") String id);

    int countByCarId(@Param("carId") String carId);

    List<TuneDto> selectRecentTunesWithDetails(int i);

    List<TuneDto> selectProTunesWithDetails(int i);
    
    /**
     * 按游戏分类查询最新调校
     * @param limit 限制数量
     * @param gameCategory 游戏分类
     * @return 最新调校列表
     */
    List<TuneDto> selectRecentTunesWithDetailsByGameCategory(@Param("limit") int limit, @Param("gameCategory") String gameCategory);
    
    /**
     * 按游戏分类查询PRO调校
     * @param limit 限制数量
     * @param gameCategory 游戏分类
     * @return PRO调校列表
     */
    List<TuneDto> selectProTunesWithDetailsByGameCategory(@Param("limit") int limit, @Param("gameCategory") String gameCategory);

    long countTotal();
    
    /**
     * 按游戏分类统计调校总数
     * @param gameCategory 游戏分类
     * @return 总数
     */
    long countTotalByGameCategory(@Param("gameCategory") String gameCategory);
}