package com.forzatune.backend.controller;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.mapper.TuneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {

    private final TuneMapper tuneMapper;

    /**
     * 获取用户点赞的调校
     */
    @GetMapping("/{userId}/likes")
    public ResponseEntity<PageDto<TuneDto>> getUserLikedTunes(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit) {
        int safePage = Math.max(page, 1);
        int safeLimit = Math.max(limit, 1);
        int offset = (safePage - 1) * safeLimit;
        long total = tuneMapper.countLikedByUser(userId);
        List<Tune> tunes = tuneMapper.selectLikedByUserPaged(userId, safeLimit, offset);
        List<TuneDto> items = tunes.stream().map(TuneDto::fromEntity).collect(Collectors.toList());
        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(safePage);
        dto.setLimit(safeLimit);
        dto.setTotal((int) total);
        dto.setTotalPages((int) Math.ceil((double) total / safeLimit));
        dto.setHasNext(safePage * safeLimit < total);
        dto.setHasPrev(safePage > 1);
        return ResponseEntity.ok(dto);
    }

    /**
     * 获取用户收藏的调校
     */
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<PageDto<TuneDto>> getUserFavoritedTunes(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit) {
        int safePage = Math.max(page, 1);
        int safeLimit = Math.max(limit, 1);
        int offset = (safePage - 1) * safeLimit;
        long total = tuneMapper.countFavoritedByUser(userId);
        List<Tune> tunes = tuneMapper.selectFavoritedByUserPaged(userId, safeLimit, offset);
        List<TuneDto> items = tunes.stream().map(TuneDto::fromEntity).collect(Collectors.toList());
        PageDto<TuneDto> dto = new PageDto<>();
        dto.setItems(items);
        dto.setPage(safePage);
        dto.setLimit(safeLimit);
        dto.setTotal((int) total);
        dto.setTotalPages((int) Math.ceil((double) total / safeLimit));
        dto.setHasNext(safePage * safeLimit < total);
        dto.setHasPrev(safePage > 1);
        return ResponseEntity.ok(dto);
    }
}