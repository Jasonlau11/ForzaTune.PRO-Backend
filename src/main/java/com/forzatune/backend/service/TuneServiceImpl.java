package com.forzatune.backend.service;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.dto.TuneParametersDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.entity.TuneParameters;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.entity.UserFavorite;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.mapper.TuneParametersMapper;
import com.forzatune.backend.mapper.UserFavoritesMapper;
import com.forzatune.backend.mapper.UserMapper;
import com.forzatune.backend.utils.RequestUtils;
import com.forzatune.backend.vo.TunesSearchVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuneServiceImpl implements TuneService {

    private final TuneMapper tuneMapper;
    private final TuneParametersMapper parametersMapper;
    private final UserFavoritesMapper favoritesMapper;
    private final UserMapper userMapper;

    @Override
    public TuneDto getTuneById(String tuneId) {
        Tune tune = tuneMapper.selectByIdWithDetail(tuneId);
        if (tune == null) {
            return null;
        }

        TuneDto tuneDto = TuneDto.fromEntity(tune);
        if (tune.getIsParametersPublic() && tune.getTuneParameters() != null) {
            tuneDto.setParameters(TuneParametersDto.fromEntity(tune.getTuneParameters()));
        }
        return tuneDto;
    }

    @Override
    @Transactional
    @CacheEvict(value = "homeDashboard", allEntries = true)
    public TuneDto createTune(TuneDto tuneDto) {
        String currentUserId = RequestUtils.getCurrentUserId();
        String currentUserXboxId = RequestUtils.getCurrentUserXboxId();

        // 如果从请求头获取不到Xbox ID，则从数据库查询用户信息
        if (currentUserXboxId.isEmpty() || currentUserXboxId.equals("dev_xbox_user")) {
            User user = userMapper.findById(currentUserId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            if (user.getXboxId() == null || user.getXboxId().isEmpty()) {
                throw new RuntimeException("当前用户未绑定Xbox ID，不能上传调校");
            }
            
            currentUserXboxId = user.getXboxId();
        }

        // 检查分享码是否已存在
        if (tuneDto.getShareCode() != null && !tuneDto.getShareCode().isEmpty()) {
            Tune existingTune = tuneMapper.selectByShareCode(tuneDto.getShareCode());
            if (existingTune != null) {
                throw new RuntimeException("当前调校已被上传，分享码: " + tuneDto.getShareCode());
            }
        }

        Tune tune = new Tune();
        BeanUtils.copyProperties(tuneDto, tune);

        String tuneId = UUID.randomUUID().toString();
        tune.setId(tuneId);
        tune.setAuthorId(currentUserId);
        tune.setAuthorXboxId(currentUserXboxId);
        tune.setIsProTune(RequestUtils.getCurrentUserIsPro());
        tune.setLikeCount(0);
        tune.setCreatedAt(new Date());
        tune.setUpdatedAt(new Date());
        
        // 从请求头获取游戏分类
        String gameCategory = RequestUtils.getCurrentGameCategory();
        tune.setGameCategory(gameCategory);

        tuneMapper.insert(tune);

        if (Objects.nonNull(tuneDto.getParameters())) {
            TuneParameters parameters = tuneDto.getParameters().toEntity();
            parameters.setId(UUID.randomUUID().toString());
            parameters.setTuneId(tuneId);
            parametersMapper.insert(parameters);
            tune.setTuneParameters(parameters);
        }

        return TuneDto.fromEntity(tune);
    }

    @Override
    @Transactional
    @CachePut(value = "carDetails", key = "#result.carId")
    @CacheEvict(value = "homeDashboard", allEntries = true)
    public TuneDto updateTune(String tuneId, TuneDto tuneDto) {
        String currentUserId = RequestUtils.getCurrentUserId();
        String currentUserXboxId = RequestUtils.getCurrentUserXboxId();

        // 如果从请求头获取不到Xbox ID，则从数据库查询用户信息
        if (currentUserXboxId == null || currentUserXboxId.isEmpty() || currentUserXboxId.equals("dev_xbox_user")) {
            User user = userMapper.findById(currentUserId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            if (user.getXboxId() == null || user.getXboxId().isEmpty()) {
                throw new RuntimeException("当前用户未绑定Xbox ID，不能修改调校");
            }
            
            currentUserXboxId = user.getXboxId();
        }

        Tune existingTune = tuneMapper.selectById(tuneId);
        if (existingTune == null) {
            throw new RuntimeException("调校不存在");
        }
        if (!existingTune.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("无权修改此调校");
        }

        // 检查分享码是否已被其他调校使用（除了当前调校）
        if (tuneDto.getShareCode() != null && !tuneDto.getShareCode().isEmpty()) {
            Tune tuneWithSameShareCode = tuneMapper.selectByShareCode(tuneDto.getShareCode());
            if (tuneWithSameShareCode != null && !tuneWithSameShareCode.getId().equals(tuneId)) {
                throw new RuntimeException("分享码已被其他调校使用: " + tuneDto.getShareCode());
            }
        }

        Tune tuneToUpdate = new Tune();
        BeanUtils.copyProperties(tuneDto, tuneToUpdate);
        tuneToUpdate.setId(tuneId);
        tuneToUpdate.setAuthorId(currentUserId);
        tuneToUpdate.setAuthorXboxId(currentUserXboxId);
        tuneMapper.update(tuneToUpdate);

        if (tuneDto.getParameters() != null) {
            TuneParameters parametersToUpdate = tuneDto.getParameters().toEntity();
            parametersToUpdate.setTuneId(tuneId);
            parametersMapper.updateByTuneId(parametersToUpdate);
        }

        return getTuneById(tuneId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"carDetails", "homeDashboard"}, allEntries = true)
    public void deleteTune(String tuneId) {
        String currentUserId = RequestUtils.getCurrentUserId();

        Tune tune = tuneMapper.selectById(tuneId);
        if (tune != null && !tune.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("无权删除此调校");
        }

        tuneMapper.deleteById(tuneId);
    }

    @Override
    public PageDto<TuneDto> getTunesByCar(TunesSearchVo searchVo) {
        // 获取总数
        long total = tuneMapper.countByCarAndConditions(searchVo);
        
        // 获取调校列表
        List<Tune> tunes = tuneMapper.selectByCarAndConditions(searchVo);
        
        // 转换为DTO
        List<TuneDto> tuneDtos = tunes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        // 构建分页信息
        PageDto<TuneDto> pageDto = new PageDto<>();
        pageDto.setItems(tuneDtos);
        pageDto.setPage(searchVo.getPage());
        pageDto.setLimit(searchVo.getLimit());
        pageDto.setTotal(total);
        pageDto.setTotalPages((int) Math.ceil((double) total / searchVo.getLimit()));
        pageDto.setHasNext(searchVo.getPage() < Math.ceil((double) total / searchVo.getLimit()));
        pageDto.setHasPrev(searchVo.getPage() > 1);
        
        return pageDto;
    }

    @Override
    @Transactional
    public Map<String, Object> likeTune(String tuneId) {
        String currentUserId = RequestUtils.getCurrentUserId();
        
        boolean isLiked = tuneMapper.isLikedByUser(tuneId, currentUserId);
        
        if (isLiked) {
            tuneMapper.removeLike(tuneId, currentUserId);
            tuneMapper.decrementLikeCount(tuneId);
        } else {
            tuneMapper.addLike(tuneId, currentUserId);
            tuneMapper.incrementLikeCount(tuneId);
        }
        
        Tune tune = tuneMapper.selectById(tuneId);
        int likeCount = tune != null ? tune.getLikeCount() : 0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("liked", !isLiked);
        result.put("likeCount", likeCount);
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> favoriteTune(String tuneId, String note) {
        String currentUserId = RequestUtils.getCurrentUserId();
        
        UserFavorite existingFavorite = favoritesMapper.selectByUserAndTune(currentUserId, tuneId);
        
        if (existingFavorite != null) {
            favoritesMapper.deleteById(existingFavorite.getId());
            tuneMapper.decrementFavoriteCount(tuneId);
        } else {
            UserFavorite favorite = new UserFavorite();
            favorite.setId(UUID.randomUUID().toString());
            favorite.setUserId(currentUserId);
            favorite.setTuneId(tuneId);
            favorite.setNote(note);
            favoritesMapper.insert(favorite);
            tuneMapper.incrementFavoriteCount(tuneId);
        }
        
        Tune tune = tuneMapper.selectById(tuneId);
//        int favoriteCount = tune != null ? tune.getFavoriteCount() : 0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("favorited", existingFavorite == null);
//        result.put("favoriteCount", favoriteCount);
        
        return result;
    }

    @Override
    public List<TuneDto> getPopularTunes(int limit) {
        List<Tune> tunes = tuneMapper.selectPopularTunes(limit);
        return tunes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TuneDto> getRecentTunes(int limit) {
        List<Tune> tunes = tuneMapper.selectRecentTunes(limit);
        return tunes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TuneDto> getProTunes(int limit) {
        List<Tune> tunes = tuneMapper.selectProTunes(limit);
        return tunes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TuneDto convertToDto(Tune tune) {
        TuneDto dto = TuneDto.fromEntity(tune);
        if (tune.getIsParametersPublic() && tune.getTuneParameters() != null) {
            dto.setParameters(TuneParametersDto.fromEntity(tune.getTuneParameters()));
        }
        return dto;
    }
}
