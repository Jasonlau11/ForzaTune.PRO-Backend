package com.forzatune.backend.mapper;

import com.forzatune.backend.dto.CarTuneCount;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.vo.TunesSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TuneMapper {

    Tune selectById(@Param("id") String id);

    Tune selectByIdWithDetail(@Param("id") String id);

    void incrementLikeCount(@Param("id") String id);
    
    /**
     * 减少点赞数
     */
    void decrementLikeCount(@Param("id") String id);
    
    /**
     * 检查用户是否已点赞
     */
    boolean isLikedByUser(@Param("tuneId") String tuneId, @Param("userId") String userId);
    
    /**
     * 添加用户点赞记录
     */
    void addLike(@Param("tuneId") String tuneId, @Param("userId") String userId);
    
    /**
     * 移除用户点赞记录
     */
    void removeLike(@Param("tuneId") String tuneId, @Param("userId") String userId);
    
    List<Tune> selectByCarId(@Param("carId") String carId);
    
    List<Tune> selectByAuthorId(@Param("authorId") String authorId);
    
    long countByAuthorId(@Param("authorId") String authorId);
    
    /**
     * 查询属于某用户的调校（owner_user_id=用户 或 owner_xbox_id=用户xboxId）
     */
    List<Tune> selectByOwner(@Param("ownerUserId") String ownerUserId, @Param("ownerXboxId") String ownerXboxId);
    
    List<Tune> selectProTunesBasic();
    
    // 新增方法：获取最新调校（包含参数）
    List<Tune> selectRecentTunesWithParameters();
    
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
    
    /**
     * 根据车辆ID和条件查询调校
     */
    List<Tune> selectByCarAndConditions(TunesSearchVo searchVo);
    
    /**
     * 根据车辆ID和条件统计调校数量
     */
    long countByCarAndConditions(TunesSearchVo searchVo);
    
    /**
     * 获取热门调校（按点赞数排序）
     */
    List<Tune> selectPopularTunes(@Param("limit") int limit);
    
    /**
     * 获取最新调校（按创建时间排序）
     */
    List<Tune> selectRecentTunes(@Param("limit") int limit);
    
    /**
     * 获取PRO调校（按点赞数排序）
     */
    List<Tune> selectProTunes(@Param("limit") int limit);
    
    /**
     * 批量查询车辆调校数量
     * @param carIds 车辆ID列表
     * @return 车辆ID和调校数量的映射
     */
    List<CarTuneCount> selectTuneCountByCarIds(@Param("carIds") List<String> carIds);
    
    /**
     * 增加收藏数
     */
    void incrementFavoriteCount(@Param("id") String id);
    
    /**
     * 减少收藏数
     */
    void decrementFavoriteCount(@Param("id") String id);
    
    /**
     * 根据分享码查询调校
     * @param shareCode 分享码
     * @return 调校信息，如果不存在则返回null
     */
    Tune selectByShareCode(@Param("shareCode") String shareCode);

    /**
     * 查询用户点赞的调校
     */
    List<Tune> selectLikedByUser(@Param("userId") String userId);
    List<Tune> selectLikedByUserPaged(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
    long countLikedByUser(@Param("userId") String userId);

    /**
     * 查询用户收藏的调校
     */
    List<Tune> selectFavoritedByUser(@Param("userId") String userId);
    List<Tune> selectFavoritedByUserPaged(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
    long countFavoritedByUser(@Param("userId") String userId);
    
    List<Tune> selectCommentedByUserPaged(@Param("tuneIds") List<String> tuneIds, @Param("limit") int limit, @Param("offset") int offset);
    long countCommentedByUser(@Param("tuneIds") List<String> tuneIds);
}