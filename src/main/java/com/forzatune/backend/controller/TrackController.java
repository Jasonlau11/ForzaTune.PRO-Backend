//package com.forzatune.backend.controller;
//
//import com.forzatune.backend.entity.Track;
//import com.forzatune.backend.mapper.TrackMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/tracks")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
//public class TrackController {
//
//    private final TrackMapper trackMapper;
//
//    /**
//     * 获取所有赛道
//     * 对应前端: getAllTracks()
//     */
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllTracks() {
//        try {
//            List<Track> tracks = trackMapper.selectAll();
//            return ResponseEntity.ok(Map.of(
//                "success", true,
//                "data", tracks
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of(
//                        "success", false,
//                        "error", Map.of(
//                            "message", "获取赛道列表失败: " + e.getMessage()
//                        )
//                    ));
//        }
//    }
//
//    /**
//     * 根据ID获取赛道详情
//     * 对应前端: getTrackById(trackId)
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Map<String, Object>> getTrackById(@PathVariable String id) {
//        try {
//            Track track = trackMapper.selectById(id);
//            if (track != null) {
//                return ResponseEntity.ok(Map.of(
//                    "success", true,
//                    "data", track
//                ));
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of(
//                        "success", false,
//                        "error", Map.of(
//                            "message", "获取赛道详情失败: " + e.getMessage()
//                        )
//                    ));
//        }
//    }
//
//    /**
//     * 根据游戏ID获取赛道
//     * 对应前端: getTracksByGameId(gameId)
//     */
//    @GetMapping("/game/{gameId}")
//    public ResponseEntity<Map<String, Object>> getTracksByGameId(@PathVariable String gameId) {
//        try {
//            List<Track> tracks = trackMapper.selectByGameId(gameId);
//            return ResponseEntity.ok(Map.of(
//                "success", true,
//                "data", tracks
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of(
//                        "success", false,
//                        "error", Map.of(
//                            "message", "获取游戏赛道失败: " + e.getMessage()
//                        )
//                    ));
//        }
//    }
//
//    /**
//     * 根据分类获取赛道
//     * 对应前端: getTracksByCategory(category)
//     */
//    @GetMapping("/category/{category}")
//    public ResponseEntity<Map<String, Object>> getTracksByCategory(@PathVariable String category) {
//        try {
//            List<Track> tracks = trackMapper.selectByCategory(category);
//            return ResponseEntity.ok(Map.of(
//                "success", true,
//                "data", tracks
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of(
//                        "success", false,
//                        "error", Map.of(
//                            "message", "获取分类赛道失败: " + e.getMessage()
//                        )
//                    ));
//        }
//    }
//
//    /**
//     * 搜索赛道
//     * 对应前端: searchTracks(keyword)
//     */
//    @GetMapping("/search")
//    public ResponseEntity<Map<String, Object>> searchTracks(@RequestParam String keyword) {
//        try {
//            List<Track> tracks = trackMapper.searchTracks(keyword);
//            return ResponseEntity.ok(Map.of(
//                "success", true,
//                "data", tracks
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of(
//                        "success", false,
//                        "error", Map.of(
//                            "message", "搜索赛道失败: " + e.getMessage()
//                        )
//                    ));
//        }
//    }
//
//    /**
//     * 获取赛道统计信息
//     * 对应前端: getTrackStats()
//     */
//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getTrackStats() {
//        try {
//            List<Track> allTracks = trackMapper.selectAll();
//
//            // 按分类统计
//            Map<String, Long> categoryStats = allTracks.stream()
//                    .collect(java.util.stream.Collectors.groupingBy(Track::getCategory, java.util.stream.Collectors.counting()));
//
//            // 按游戏统计
//            Map<String, Long> gameStats = allTracks.stream()
//                    .collect(java.util.stream.Collectors.groupingBy(Track::getGameId, java.util.stream.Collectors.counting()));
//
//            Map<String, Object> stats = Map.of(
//                "totalTracks", allTracks.size(),
//                "categoryStats", categoryStats,
//                "gameStats", gameStats
//            );
//
//            return ResponseEntity.ok(Map.of(
//                "success", true,
//                "data", stats
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of(
//                        "success", false,
//                        "error", Map.of(
//                            "message", "获取赛道统计信息失败: " + e.getMessage()
//                        )
//                    ));
//        }
//    }
//}