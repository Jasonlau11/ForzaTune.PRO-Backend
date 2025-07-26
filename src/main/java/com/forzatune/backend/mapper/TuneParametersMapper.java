package com.forzatune.backend.mapper;

import com.forzatune.backend.entity.TuneParameters;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 调校参数数据访问接口
 */
@Mapper
public interface TuneParametersMapper {

    /**
     * 插入一套新的调校参数。
     * @param tuneParameters 要插入的参数实体对象。
     * @return 影响的行数。
     */
    int insert(TuneParameters tuneParameters);

    /**
     * 根据关联的 tuneId 更新一套已存在的调校参数。
     * @param tuneParameters 包含更新数据的参数实体对象。
     * @return 影响的行数。
     */
    int updateByTuneId(TuneParameters tuneParameters);

    /**
     * 根据关联的 tuneId 查询调校参数。
     * @param tuneId 调校的ID。
     * @return TuneParameters 实体对象，如果不存在则返回 null。
     */
    TuneParameters selectByTuneId(String tuneId);

    /**
     * 根据关联的 tuneId 删除调校参数。
     * @param tuneId 调校的ID。
     * @return 影响的行数。
     */
    int deleteByTuneId(String tuneId);
}
