package com.forzatune.backend.service;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.entity.User;
import com.forzatune.backend.entity.UserActivity;
import com.forzatune.backend.entity.UserFavorite;
import com.forzatune.backend.mapper.ActivityMapper;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuneServiceImpl implements TuneService {

    private final TuneMapper tuneMapper;
    private final UserFavoritesMapper favoritesMapper;
    private final UserMapper userMapper;
    private final ActivityMapper activityMapper;

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

        // 归属逻辑：如果未传 owner 或者传的是当前用户的已验证 xboxId，则直接 verified；否则标记 unverified
        String requestedOwnerXboxId = tuneDto.getOwnerXboxId();
        tune.setOwnerUserId(null);
        tune.setOwnerXboxId(null);
        tune.setOwnershipStatus("unverified");
        tune.setOwnerVerifiedAt(null);

        User currentUser = userMapper.findById(currentUserId);
        boolean userVerified = currentUser != null &&
                currentUser.getXboxVerificationStatus() != null &&
                currentUser.getXboxVerificationStatus().equals("approved");

        if (requestedOwnerXboxId == null || requestedOwnerXboxId.trim().isEmpty()) {
            // 默认归属自己
            tune.setOwnerUserId(currentUserId);
            tune.setOwnerXboxId(currentUserXboxId);
            tune.setOwnershipStatus("verified");
            tune.setOwnerVerifiedAt(new Date());
        } else if (userVerified && requestedOwnerXboxId.equalsIgnoreCase(currentUser.getXboxId())) {
            // 指定自己且已验证
            tune.setOwnerUserId(currentUserId);
            tune.setOwnerXboxId(currentUser.getXboxId());
            tune.setOwnershipStatus("verified");
            tune.setOwnerVerifiedAt(new Date());
        } else {
            // 指定他人，进入未验证
            tune.setOwnerUserId(null);
            tune.setOwnerXboxId(requestedOwnerXboxId);
            tune.setOwnershipStatus("unverified");
            tune.setOwnerVerifiedAt(null);
        }

        // 处理详细参数
        if (tuneDto.getParameters() != null) {
            tune.setHasDetailedParameters(true);
            // parameters字段已经通过BeanUtils.copyProperties复制了
        } else {
            tune.setHasDetailedParameters(false);
        }

        tuneMapper.insert(tune);

        // 记录上传活动
        try {
            User me = userMapper.findById(currentUserId);
            UserActivity act = new UserActivity();
            act.setId(UUID.randomUUID().toString());
            act.setUserId(currentUserId);
            act.setUserXboxId(me != null ? me.getXboxId() : null);
            act.setType(UserActivity.ActivityType.UPLOAD);
            act.setTargetId(tuneId);
            act.setTargetName(null);
            act.setDescription("上传调校");
            activityMapper.insert(act);
        } catch (Exception ignore) {}

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
        boolean proOnly = Boolean.TRUE.equals(searchVo.getProOnly());

        List<TuneDto> tuneDtos;
        long total;

        if (proOnly) {
            // 为确保基于“归属人为PRO”的精确筛选与分页，先取全量后内存过滤并分页
            List<Tune> allTunes = tuneMapper.selectByCarId(searchVo.getCarId());
            List<TuneDto> allDtos = allTunes.stream().map(TuneDto::fromEntity).collect(Collectors.toList());

            // 标注 ownerIsPro
            for (TuneDto dto : allDtos) {
                boolean ownerPro = false;
                if (dto.getOwnerUserId() != null && !dto.getOwnerUserId().isEmpty()) {
                    User owner = userMapper.findById(dto.getOwnerUserId());
                    ownerPro = owner != null && Boolean.TRUE.equals(owner.getIsProPlayer());
                }
                dto.setOwnerIsPro(ownerPro);
            }

            List<TuneDto> filtered = allDtos.stream()
                    .filter(d -> Boolean.TRUE.equals(d.getOwnerIsPro()))
                    .collect(Collectors.toList());

            total = filtered.size();

            int page = searchVo.getPage() <= 0 ? 1 : searchVo.getPage();
            int limit = searchVo.getLimit() <= 0 ? 12 : searchVo.getLimit();
            int from = Math.max(0, (page - 1) * limit);
            int to = Math.min((int) total, from + limit);
            tuneDtos = from >= total ? Collections.emptyList() : filtered.subList(from, to);
        } else {
            // 常规路径：数据库分页 + 轻量标注 ownerIsPro（不影响查询结果集）
            long dbTotal = tuneMapper.countByCarAndConditions(searchVo);
            List<Tune> tunes = tuneMapper.selectByCarAndConditions(searchVo);
            tuneDtos = tunes.stream().map(TuneDto::fromEntity).collect(Collectors.toList());
            for (TuneDto dto : tuneDtos) {
                boolean ownerPro = false;
                if (dto.getOwnerUserId() != null && !dto.getOwnerUserId().isEmpty()) {
                    User owner = userMapper.findById(dto.getOwnerUserId());
                    ownerPro = owner != null && Boolean.TRUE.equals(owner.getIsProPlayer());
                }
                dto.setOwnerIsPro(ownerPro);
            }
            total = dbTotal;
        }

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
            Tune t = tuneMapper.selectById(tuneId);
            result.put("likeCount", t != null ? t.getLikeCount() : 0);
            result.put("message", "取消点赞成功");
            return result;
        } else {
            // 添加点赞
            tuneMapper.addLike(tuneId, currentUserId);
            tuneMapper.incrementLikeCount(tuneId);
            // 记录活动
            try {
                User me = userMapper.findById(currentUserId);
                UserActivity act = new UserActivity();
                act.setId(UUID.randomUUID().toString());
                act.setUserId(currentUserId);
                act.setUserXboxId(me != null ? me.getXboxId() : null);
                act.setType(UserActivity.ActivityType.LIKE);
                act.setTargetId(tuneId);
                act.setTargetName(null);
                act.setDescription("点赞调校");
                activityMapper.insert(act);
            } catch (Exception ignore) {}
            Map<String, Object> result = new HashMap<>();
            result.put("liked", true);
            Tune t = tuneMapper.selectById(tuneId);
            result.put("likeCount", t != null ? t.getLikeCount() : 0);
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
            Map<String, Object> result = new HashMap<>();
            result.put("favorited", false);
            int count = favoritesMapper.countByTuneId(tuneId);
            result.put("favoriteCount", count);
            result.put("message", "取消收藏成功");
            return result;
        } else {
            // 添加收藏
            UserFavorite favorite = new UserFavorite();
            favorite.setId(UUID.randomUUID().toString());
            favorite.setUserId(currentUserId);
            favorite.setTuneId(tuneId);
            // 当前表结构无note列
            favoritesMapper.insert(favorite);
            // 记录活动
            try {
                User me = userMapper.findById(currentUserId);
                UserActivity act = new UserActivity();
                act.setId(UUID.randomUUID().toString());
                act.setUserId(currentUserId);
                act.setUserXboxId(me != null ? me.getXboxId() : null);
                act.setType(UserActivity.ActivityType.FAVORITE);
                act.setTargetId(tuneId);
                act.setTargetName(null);
                act.setDescription("收藏调校");
                activityMapper.insert(act);
            } catch (Exception ignore) {}
            Map<String, Object> result = new HashMap<>();
            result.put("favorited", true);
            int count = favoritesMapper.countByTuneId(tuneId);
            result.put("favoriteCount", count);
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

    @Override
    public PageDto<TuneDto> getMyTunes(String userId, int page, int limit, String gameCategory) {
        // 直接利用已有的按作者查询，再在内存中做简单分页（数据量通常不大）。
        List<Tune> all = tuneMapper.selectByAuthorId(userId);
        if (gameCategory != null && !gameCategory.isEmpty()) {
            all = all.stream().filter(t -> gameCategory.equals(t.getGameCategory())).collect(Collectors.toList());
        }

        int total = all.size();
        int from = Math.max(0, (page - 1) * limit);
        int to = Math.min(total, from + limit);
        List<Tune> pageItems = from >= total ? Collections.emptyList() : all.subList(from, to);
        List<TuneDto> items = pageItems.stream().map(TuneDto::fromEntity).collect(Collectors.toList());

        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(page);
        dto.setLimit(limit);
        dto.setTotal(total);
        dto.setTotalPages((int)Math.ceil((double) total / limit));
        dto.setHasNext(page < dto.getTotalPages());
        dto.setHasPrev(page > 1);
        return dto;
    }

    @Override
    public PageDto<TuneDto> getOwnedTunes(String userId, int page, int limit, String gameCategory) {
        User me = userMapper.findById(userId);
        String myXbox = (me != null && me.getXboxId() != null) ? me.getXboxId() : null;

        List<Tune> all = tuneMapper.selectByOwner(userId, myXbox);
        if (gameCategory != null && !gameCategory.isEmpty()) {
            all = all.stream().filter(t -> gameCategory.equals(t.getGameCategory())).collect(Collectors.toList());
        }

        int total = all.size();
        int from = Math.max(0, (page - 1) * limit);
        int to = Math.min(total, from + limit);
        List<Tune> pageItems = from >= total ? Collections.emptyList() : all.subList(from, to);
        List<TuneDto> items = pageItems.stream().map(TuneDto::fromEntity).collect(Collectors.toList());

        // 标注 ownerIsPro
        for (TuneDto dto : items) {
            boolean ownerPro = false;
            if (dto.getOwnerUserId() != null && !dto.getOwnerUserId().isEmpty()) {
                User owner = userMapper.findById(dto.getOwnerUserId());
                ownerPro = owner != null && Boolean.TRUE.equals(owner.getIsProPlayer());
            }
            dto.setOwnerIsPro(ownerPro);
        }

        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(page);
        dto.setLimit(limit);
        dto.setTotal(total);
        dto.setTotalPages((int) Math.ceil((double) total / limit));
        dto.setHasNext(page < dto.getTotalPages());
        dto.setHasPrev(page > 1);
        return dto;
    }
}
