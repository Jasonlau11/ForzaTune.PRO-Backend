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
    
    List<Tune> selectByPreference(@Param("preference") String preference);
    
    List<Tune> selectByPiClass(@Param("piClass") String piClass);
    
    List<Tune> selectByRaceType(@Param("raceType") String raceType);
    
    List<Tune> selectProTunesByCarId(@Param("carId") String carId);
    
    List<Tune> selectByCarIdAndPreference(@Param("carId") String carId, @Param("preference") String preference);
    
    // 新增方法：获取热门调校（按点赞数排序）
    List<Tune> selectPopularTunes();
    
    // 新增方法：获取最新调校
    List<Tune> selectRecentTunes();
    
    // 新增方法：搜索调校（支持多种条件）
    List<Tune> searchTunes(@Param("keyword") String keyword,
                          @Param("carId") String carId,
                          @Param("isProTune") Boolean isProTune,
                          @Param("preference") String preference,
                          @Param("piClass") String piClass,
                          @Param("raceType") String raceType,
                          @Param("sortBy") String sortBy,
                          @Param("limit") Integer limit);
    
    int insert(Tune tune);
    
    int update(Tune tune);
    
    int deleteById(@Param("id") String id);
    
    int updateLikeCount(@Param("id") String id, @Param("likeCount") Integer likeCount);
    
    int countByCarId(@Param("carId") String carId);
    
    int countByAuthorId(@Param("authorId") String authorId);

    List<TuneDto> selectRecentTunesWithDetails(int i);

    List<TuneDto> selectProTunesWithDetails(int i);

    long countTotal();
}