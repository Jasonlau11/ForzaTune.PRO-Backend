package com.forzatune.backend.service;

import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.dto.TuneParametersDto;
import com.forzatune.backend.dto.TuneSubmissionDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.entity.TuneParameters;
import com.forzatune.backend.entity.UserFavorite;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.mapper.TuneParametersMapper;
import com.forzatune.backend.mapper.UserFavoritesMapper;
import com.forzatune.backend.utils.RequestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class TuneServiceImpl implements TuneService {

    private final TuneMapper tuneMapper;
    private final TuneParametersMapper parametersMapper;
    private final UserFavoritesMapper favoritesMapper;

    @Autowired
    public TuneServiceImpl(TuneMapper tuneMapper, TuneParametersMapper parametersMapper, UserFavoritesMapper favoritesMapper) {
        this.tuneMapper = tuneMapper;
        this.parametersMapper = parametersMapper;
        this.favoritesMapper = favoritesMapper;
    }

    @Override
    public TuneDto getTuneByIdWithDetail(String id) {
        Tune tune = tuneMapper.selectByIdWithDetail(id);
        TuneDto tuneDto = TuneDto.fromEntity(tune); // Assuming TuneDto has a suitable fromEntity method
        if (tune != null && tune.getTuneParameters() != null) {
            tuneDto.setParameters(TuneParametersDto.fromEntity(tune.getTuneParameters()));
        }
        return tuneDto;
    }

    @Override
    @Transactional // 开启事务，保证 tune 和 parameters 要么都成功，要么都失败
    @CacheEvict(value = "homeDashboard", allEntries = true) // 新增调校后，清除首页缓存
    public Tune createTune(TuneSubmissionDto tuneDto) {
        String currentUserId = RequestUtils.getCurrentUserId();
        // String currentUserGamertag = ... (从用户信息中获取)

        Tune tune = new Tune();
        BeanUtils.copyProperties(tuneDto, tune); // 复制基础属性

        // 设置后端控制的属性
        String tuneId = UUID.randomUUID().toString();
        tune.setId(tuneId);
        tune.setAuthorId(currentUserId);
        // tune.setAuthorGamertag(currentUserGamertag);
        tune.setLikeCount(0);

        tuneMapper.insert(tune);

        // 如果提交了详细参数，则一并插入
        if (Objects.nonNull(tuneDto.getParameters())) {
            TuneParameters parameters = tuneDto.getParameters().toEntity();
            parameters.setId(UUID.randomUUID().toString());
            parameters.setTuneId(tuneId);
            parametersMapper.insert(parameters);
            tune.setTuneParameters(parameters);
        }

        return tune;
    }

    @Override
    @Transactional
    @CachePut(value = "carDetails", key = "#result.carId") // 更新车辆详情缓存
    @CacheEvict(value = "homeDashboard", allEntries = true) // 更新后可能影响首页，清除缓存
    public Tune updateTune(String tuneId, TuneSubmissionDto tuneDto) {
        String currentUserId = RequestUtils.getCurrentUserId();

        // 验证操作权限：确保是调校的作者本人在修改
        Tune existingTune = tuneMapper.selectById(tuneId);
        if (existingTune == null) {
            throw new RuntimeException("调校不存在");
        }
        if (!existingTune.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("无权修改此调校");
        }

        // 更新 Tune 主表
        Tune tuneToUpdate = new Tune();
        BeanUtils.copyProperties(tuneDto, tuneToUpdate);
        tuneToUpdate.setId(tuneId);
        tuneToUpdate.setAuthorId(currentUserId); // 确保 authorId 不会变
        tuneMapper.update(tuneToUpdate);

        // 更新 TuneParameters 表
        if (tuneDto.getParameters() != null) {
            TuneParameters parametersToUpdate = tuneDto.getParameters().toEntity();
            parametersToUpdate.setTuneId(tuneId); // 确保 tuneId 正确
            parametersMapper.updateByTuneId(parametersToUpdate);
        }

        return tuneMapper.selectByIdWithDetail(tuneId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"carDetails", "homeDashboard"}, allEntries = true) // 删除后清除所有相关缓存
    public void deleteTune(String tuneId) {
        String currentUserId = RequestUtils.getCurrentUserId();

        Tune tune = tuneMapper.selectById(tuneId);
        if (tune != null && !tune.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("无权删除此调校");
        }

        // 在数据库层面设置了 ON DELETE CASCADE, 删除 tune 会自动删除 tune_parameters
        tuneMapper.deleteById(tuneId);
    }

    @Override
    public void likeTune(String id) {
        tuneMapper.incrementLikeCount(id);
    }

    @Override
    public void favoriteTune(String id) {
        String currentUserId = RequestUtils.getCurrentUserId();
        UserFavorite favorite = new UserFavorite();
        favorite.setTuneId(id);
        favorite.setUserId(currentUserId);
        favorite.setId(UUID.randomUUID().toString());
        favoritesMapper.insert(favorite);
    }
}
