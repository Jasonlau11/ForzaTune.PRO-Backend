package com.forzatune.backend.service;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.entity.Car;
import com.forzatune.backend.mapper.CommentMapper;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.mapper.CarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TuneMapper tuneMapper;
    private final CommentMapper commentMapper;
    private final CarMapper carMapper;

    /**
     * 获取用户点赞的调校
     */
    public PageDto<TuneDto> getUserLikedTunes(String userId, int page, int limit) {
        int safePage = Math.max(page, 1);
        int safeLimit = Math.max(limit, 1);
        int offset = (safePage - 1) * safeLimit;
        
        long total = tuneMapper.countLikedByUser(userId);
        List<Tune> tunes = tuneMapper.selectLikedByUserPaged(userId, safeLimit, offset);
        List<TuneDto> items = tunes.stream().map(tune -> {
            String carName = getCarName(tune.getCarId());
            return TuneDto.fromEntity(tune, carName);
        }).collect(Collectors.toList());
        
        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(safePage);
        dto.setLimit(safeLimit);
        dto.setTotal((int) total);
        dto.setTotalPages((int) Math.ceil((double) total / safeLimit));
        dto.setHasNext(safePage * safeLimit < total);
        dto.setHasPrev(safePage > 1);
        
        return dto;
    }

    /**
     * 获取用户收藏的调校
     */
    public PageDto<TuneDto> getUserFavoritedTunes(String userId, int page, int limit) {
        int safePage = Math.max(page, 1);
        int safeLimit = Math.max(limit, 1);
        int offset = (safePage - 1) * safeLimit;
        
        long total = tuneMapper.countFavoritedByUser(userId);
        List<Tune> tunes = tuneMapper.selectFavoritedByUserPaged(userId, safeLimit, offset);
        List<TuneDto> items = tunes.stream().map(tune -> {
            String carName = getCarName(tune.getCarId());
            return TuneDto.fromEntity(tune, carName);
        }).collect(Collectors.toList());
        
        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(safePage);
        dto.setLimit(safeLimit);
        dto.setTotal((int) total);
        dto.setTotalPages((int) Math.ceil((double) total / safeLimit));
        dto.setHasNext(safePage * safeLimit < total);
        dto.setHasPrev(safePage > 1);
        
        return dto;
    }

    /**
     * 获取用户评论过的调校
     */
    public PageDto<TuneDto> getUserCommentedTunes(String userId, int page, int limit) {
        int safePage = Math.max(page, 1);
        int safeLimit = Math.max(limit, 1);
        int offset = (safePage - 1) * safeLimit;
        
        // 先获取用户评论过的调校ID列表
        List<String> commentedTuneIds = commentMapper.selectTuneIdsByUserId(userId);
        
        if (commentedTuneIds.isEmpty()) {
            // 如果没有评论过任何调校，返回空结果
            PageDto<TuneDto> emptyDto = new PageDto<>();
            emptyDto.setItems(new ArrayList<>());
            emptyDto.setPage(safePage);
            emptyDto.setLimit(safeLimit);
            emptyDto.setTotal(0);
            emptyDto.setTotalPages(0);
            emptyDto.setHasNext(false);
            emptyDto.setHasPrev(false);
            return emptyDto;
        }
        
        // 根据调校ID列表查询调校信息
        long total = tuneMapper.countCommentedByUser(commentedTuneIds);
        List<Tune> tunes = tuneMapper.selectCommentedByUserPaged(commentedTuneIds, safeLimit, offset);
        List<TuneDto> items = tunes.stream().map(tune -> {
            String carName = getCarName(tune.getCarId());
            return TuneDto.fromEntity(tune, carName);
        }).collect(Collectors.toList());
        
        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(safePage);
        dto.setLimit(safeLimit);
        dto.setTotal((int) total);
        dto.setTotalPages((int) Math.ceil((double) total / safeLimit));
        dto.setHasNext(safePage * safeLimit < total);
        dto.setHasPrev(safePage > 1);
        
        return dto;
    }

    /**
     * 根据车型ID获取车型名称
     */
    private String getCarName(String carId) {
        if (carId == null) {
            return "Unknown Car";
        }
        try {
            Car car = carMapper.selectById(carId);
            if (car != null) {
                return car.getYear() + " " + car.getManufacturer() + " " + car.getName();
            }
        } catch (Exception e) {
            // 忽略异常，返回默认值
        }
        return "Unknown Car";
    }
}