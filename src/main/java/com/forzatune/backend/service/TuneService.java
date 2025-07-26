package com.forzatune.backend.service;

import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.dto.TuneSubmissionDto;
import com.forzatune.backend.entity.Tune;

/**
 * @Description :
 * @Author 10331332
 * @Date 2025/07/25/上午9:12
 */

public interface TuneService {

    /**
     * 根据ID获取调校详情，包含参数
     */
    TuneDto getTuneByIdWithDetail(String id);

    /**
     * 创建一个新的调校
     * @param tuneDto 前端提交的数据
     * @return 创建成功后的 Tune 实体
     */
    Tune createTune(TuneSubmissionDto tuneDto);

    /**
     * 更新一个已存在的调校
     * @param tuneId 要更新的调校ID
     * @param tuneDto 前端提交的更新数据
     * @return 更新成功后的 Tune 实体
     */
    Tune updateTune(String tuneId, TuneSubmissionDto tuneDto);

    /**
     * 删除一个调校
     * @param tuneId 要删除的调校ID
     */
    void deleteTune(String tuneId);

    /**
     * 为调校点赞
     */
    void likeTune(String id);

    /**
     * 收藏调校
     */
    void favoriteTune(String id);
}
