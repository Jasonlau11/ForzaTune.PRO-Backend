package com.forzatune.backend.service;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.entity.UserFavorite;
import com.forzatune.backend.mapper.TuneMapper;
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
    private final UserFavoritesMapper favoritesMapper;
    private final UserMapper userMapper;

    @Override
    public TuneDto getTuneById(String tuneId) {
        Tune tune = tuneMapper.selectByIdWithDetail(tuneId);
        if (tune == null) {
            return null;
        }

        return TuneDto.fromEntity(tune);
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

        // 处理详细参数
        if (tuneDto.getParameters() != null) {
            tune.setHasDetailedParameters(true);
            // parameters字段已经通过BeanUtils.copyProperties复制了
        } else {
            tune.setHasDetailedParameters(false);
        }

        tuneMapper.insert(tune);

        return TuneDto.fromEntity(tune);
    }

    @Override
    @Transactional
    @CachePut(value = "tunes", key = "#tuneId")
    public TuneDto updateTune(String tuneId, TuneDto tuneDto) {
        String currentUserId = RequestUtils.getCurrentUserId();
        
        Tune existingTune = tuneMapper.selectById(tuneId);
        if (existingTune == null) {
            throw new RuntimeException("调校不存在");
        }
        
        // 检查权限
        if (!existingTune.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("只能修改自己的调校");
        }

        // 更新调校信息
        BeanUtils.copyProperties(tuneDto, existingTune);
        existingTune.setUpdatedAt(new Date());
        
        tuneMapper.update(existingTune);
        
        return TuneDto.fromEntity(existingTune);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tunes", key = "#tuneId")
    public void deleteTune(String tuneId) {
        String currentUserId = RequestUtils.getCurrentUserId();
        
        Tune tune = tuneMapper.selectById(tuneId);
        if (tune == null) {
            throw new RuntimeException("调校不存在");
        }
        
        // 检查权限
        if (!tune.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("只能删除自己的调校");
        }
        
        tuneMapper.deleteById(tuneId);
    }

    @Override
    public PageDto<TuneDto> getTunesByCar(TunesSearchVo searchVo) {
        // 获取总数
        long total = tuneMapper.countByCarAndConditions(searchVo);
        
        // 获取数据
        List<Tune> tunes = tuneMapper.selectByCarAndConditions(searchVo);
        List<TuneDto> tuneDtos = tunes.stream()
                .map(TuneDto::fromEntity)
                .collect(Collectors.toList());
        
        PageDto<TuneDto> pageDto = new PageDto<>();
        pageDto.setItems(tuneDtos);
        pageDto.setTotal(total);
        pageDto.setPage(searchVo.getPage());
        pageDto.setLimit(searchVo.getLimit());
        return pageDto;
    }

    @Override
    @Transactional
    public Map<String, Object> likeTune(String tuneId) {
        String currentUserId = RequestUtils.getCurrentUserId();
        
        // 检查是否已点赞
        boolean isLiked = tuneMapper.isLikedByUser(tuneId, currentUserId);
        
        if (isLiked) {
            // 取消点赞
            tuneMapper.removeLike(tuneId, currentUserId);
            tuneMapper.decrementLikeCount(tuneId);
            Map<String, Object> result = new HashMap<>();
            result.put("liked", false);
            result.put("message", "取消点赞成功");
            return result;
        } else {
            // 添加点赞
            tuneMapper.addLike(tuneId, currentUserId);
            tuneMapper.incrementLikeCount(tuneId);
            Map<String, Object> result = new HashMap<>();
            result.put("liked", true);
            result.put("message", "点赞成功");
            return result;
        }
    }

    @Override
    @Transactional
    public Map<String, Object> favoriteTune(String tuneId, String note) {
        String currentUserId = RequestUtils.getCurrentUserId();
        
        // 检查是否已收藏
        UserFavorite existingFavorite = favoritesMapper.findByUserAndTune(currentUserId, tuneId);
        
        if (existingFavorite != null) {
            // 取消收藏
            favoritesMapper.delete(currentUserId, tuneId);
            tuneMapper.decrementFavoriteCount(tuneId);
            Map<String, Object> result = new HashMap<>();
            result.put("favorited", false);
            result.put("message", "取消收藏成功");
            return result;
        } else {
            // 添加收藏
            UserFavorite favorite = new UserFavorite();
            favorite.setId(UUID.randomUUID().toString());
            favorite.setUserId(currentUserId);
            favorite.setTuneId(tuneId);
            favorite.setNote(note);
            favoritesMapper.insert(favorite);
            tuneMapper.incrementFavoriteCount(tuneId);
            Map<String, Object> result = new HashMap<>();
            result.put("favorited", true);
            result.put("message", "收藏成功");
            return result;
        }
    }

    @Override
    public List<TuneDto> getRecentTunes(int limit) {
        List<Tune> tunes = tuneMapper.selectRecentTunes(limit);
        return tunes.stream()
                .map(TuneDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TuneDto> getProTunes(int limit) {
        List<Tune> tunes = tuneMapper.selectProTunes(limit);
        return tunes.stream()
                .map(TuneDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TuneDto> getPopularTunes(int limit) {
        List<Tune> tunes = tuneMapper.selectPopularTunes(limit);
        return tunes.stream()
                .map(TuneDto::fromEntity)
                .collect(Collectors.toList());
    }
}
