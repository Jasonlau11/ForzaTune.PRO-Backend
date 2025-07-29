package com.forzatune.backend.service;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.vo.TunesSearchVo;

import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author 10331332
 * @Date 2025/07/25/上午9:12
 */

public interface TuneService {

    /**
     * 创建调校
     */
    TuneDto createTune(TuneDto tuneDto);

    /**
     * 更新调校
     */
    TuneDto updateTune(String tuneId, TuneDto tuneDto);

    /**
     * 删除调校
     */
    void deleteTune(String tuneId);

    /**
     * 根据ID获取调校详情
     */
    TuneDto getTuneById(String tuneId);

    /**
     * 根据车辆ID获取调校列表
     */
    PageDto<TuneDto> getTunesByCar(TunesSearchVo searchVo);

    /**
     * 点赞调校
     */
    Map<String, Object> likeTune(String tuneId);

    /**
     * 收藏调校
     */
    Map<String, Object> favoriteTune(String tuneId, String note);

    /**
     * 获取热门调校
     */
    List<TuneDto> getPopularTunes(int limit);

    /**
     * 获取最新调校
     */
    List<TuneDto> getRecentTunes(int limit);

    /**
     * 获取PRO调校
     */
    List<TuneDto> getProTunes(int limit);
}
