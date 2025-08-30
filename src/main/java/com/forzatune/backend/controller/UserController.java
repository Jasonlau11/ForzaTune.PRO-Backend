package com.forzatune.backend.controller;

import com.forzatune.backend.dto.PageDto;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {

    private final UserService userService;

    /**
     * 获取用户点赞的调校
     */
    @GetMapping("/{userId}/likes")
    public ResponseEntity<PageDto<TuneDto>> getUserLikedTunes(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit) {
        PageDto<TuneDto> result = userService.getUserLikedTunes(userId, page, limit);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户收藏的调校
     */
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<PageDto<TuneDto>> getUserFavoritedTunes(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit) {
        PageDto<TuneDto> result = userService.getUserFavoritedTunes(userId, page, limit);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户评论过的调校
     */
    @GetMapping("/{userId}/comments")
    public ResponseEntity<PageDto<TuneDto>> getUserCommentedTunes(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit) {
        PageDto<TuneDto> result = userService.getUserCommentedTunes(userId, page, limit);
        return ResponseEntity.ok(result);
    }
}