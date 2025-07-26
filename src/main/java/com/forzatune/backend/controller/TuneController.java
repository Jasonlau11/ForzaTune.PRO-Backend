package com.forzatune.backend.controller;

import com.forzatune.backend.dto.ApiResponse;
import com.forzatune.backend.dto.TuneDto;
import com.forzatune.backend.dto.TuneSubmissionDto;
import com.forzatune.backend.entity.Tune;
import com.forzatune.backend.mapper.TuneMapper;
import com.forzatune.backend.service.TuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tunes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TuneController {
    @Autowired
    private TuneService tuneService;

    /**
     * 新增一个调校
     */
    @PostMapping
    public ApiResponse<Tune> createTune(@RequestBody TuneSubmissionDto tuneDto) {
        try {
            Tune createdTune = tuneService.createTune(tuneDto);
            return ApiResponse.success(createdTune);
        } catch (Exception e) {
            return ApiResponse.failure("创建调校失败: " + e.getMessage());
        }
    }

    /**
     * 更新一个调校
     */
    @PutMapping("/{id}")
    public ApiResponse<Tune> updateTune(@PathVariable String id, @RequestBody TuneSubmissionDto tuneDto) {
        try {
            Tune updatedTune = tuneService.updateTune(id, tuneDto);
            return ApiResponse.success(updatedTune);
        } catch (Exception e) {
            return ApiResponse.failure("更新调校失败: " + e.getMessage());
        }
    }

    /**
     * 删除一个调校
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTune(@PathVariable String id) {
        try {
            tuneService.deleteTune(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.failure("删除调校失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取调校详情
     * 对应前端: getTuneById(tuneId)
     */
    @GetMapping("/{id}")
    public ApiResponse<TuneDto> getTuneById(@PathVariable String id) {
        try {
            return ApiResponse.success(tuneService.getTuneByIdWithDetail(id));
        } catch (Exception e) {
            return ApiResponse.failure("获取调校数据失败: " + e.getMessage());
        }
    }

    /**
     * 调校点赞
     * 对应前端: likeTune(tuneId)
     */
    @PostMapping("/{id}/like")
    public ApiResponse<String> likeTune(@PathVariable String id) {
        try {
            tuneService.likeTune(id);
            return ApiResponse.success("ok");
        } catch (Exception e) {
            return ApiResponse.failure("点赞调校失败: " + e.getMessage());
        }
    }
    
    /**
     * 调校收藏/取消收藏
     * 对应前端: favoriteTune(tuneId)
     */
    @PostMapping("/{id}/favorite")
    public ApiResponse<String> favoriteTune(@PathVariable String id) {
        try {
            tuneService.favoriteTune(id);
            return ApiResponse.success("ok");
        } catch (Exception e) {
            return ApiResponse.failure("收藏调校失败: " + e.getMessage());
        }
    }

} 