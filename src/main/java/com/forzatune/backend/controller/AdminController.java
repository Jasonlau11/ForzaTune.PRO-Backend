//package com.forzatune.backend.controller;
//
//import com.forzatune.backend.service.CarDataService;
//import com.forzatune.backend.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/admin")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
//public class AdminController {
//
//    private final CarDataService carDataService;
//    private final UserService userService;
//
//    /**
//     * 重新加载车型数据
//     * POST /admin/cars/reload
//     */
//    @PostMapping("/cars/reload")
//    public ResponseEntity<Map<String, String>> reloadCarData() {
//        try {
//            carDataService.reloadData();
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "车型数据重新加载成功");
//            response.put("status", "success");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("Failed to reload car data", e);
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "车型数据重新加载失败: " + e.getMessage());
//            response.put("status", "error");
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    /**
//     * 获取车型数据统计
//     * GET /admin/cars/stats
//     */
//    @GetMapping("/cars/stats")
//    public ResponseEntity<Map<String, Object>> getCarStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("dataInfo", carDataService.getDataInfo());
//        stats.put("totalCars", carDataService.getAllCars().size());
//        stats.put("manufacturers", carDataService.getAllManufacturers().size());
//        return ResponseEntity.ok(stats);
//    }
//
//    /**
//     * 清理无效用户
//     * POST /admin/users/cleanup
//     */
//    @PostMapping("/users/cleanup")
//    public ResponseEntity<Map<String, Object>> cleanupUsers(
//            @RequestParam(defaultValue = "false") boolean dryRun) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            // 这里可以添加清理逻辑，比如删除长期未登录的用户
//            // 暂时返回模拟数据
//            result.put("message", dryRun ? "模拟清理完成" : "用户清理完成");
//            result.put("dryRun", dryRun);
//            result.put("cleanedCount", 0);
//            result.put("status", "success");
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Failed to cleanup users", e);
//            result.put("message", "用户清理失败: " + e.getMessage());
//            result.put("status", "error");
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
//
//    /**
//     * 清理无效调校
//     * POST /admin/tunes/cleanup
//     */
//    @PostMapping("/tunes/cleanup")
//    public ResponseEntity<Map<String, Object>> cleanupTunes(
//            @RequestParam(defaultValue = "false") boolean dryRun,
//            @RequestParam(defaultValue = "0") int daysThreshold) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            // 这里可以添加清理逻辑，比如删除长期无人使用的调校
//            result.put("message", dryRun ? "模拟清理完成" : "调校清理完成");
//            result.put("dryRun", dryRun);
//            result.put("daysThreshold", daysThreshold);
//            result.put("cleanedCount", 0);
//            result.put("status", "success");
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Failed to cleanup tunes", e);
//            result.put("message", "调校清理失败: " + e.getMessage());
//            result.put("status", "error");
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
//
//    /**
//     * 系统健康检查
//     * GET /admin/health
//     */
//    @GetMapping("/health")
//    public ResponseEntity<Map<String, Object>> healthCheck() {
//        Map<String, Object> health = new HashMap<>();
//
//        try {
//            // 检查车型数据
//            int carCount = carDataService.getAllCars().size();
//            health.put("carData", Map.of(
//                "status", "healthy",
//                "count", carCount,
//                "version", carDataService.getDataInfo().get("version")
//            ));
//
//            // 检查用户数据
//            long userCount = userService.getProPlayerCount();
//            health.put("userData", Map.of(
//                "status", "healthy",
//                "proPlayerCount", userCount
//            ));
//
//            health.put("overall", "healthy");
//            health.put("timestamp", System.currentTimeMillis());
//
//            return ResponseEntity.ok(health);
//        } catch (Exception e) {
//            log.error("Health check failed", e);
//            health.put("overall", "unhealthy");
//            health.put("error", e.getMessage());
//            return ResponseEntity.status(503).body(health);
//        }
//    }
//
//    /**
//     * 批量操作接口
//     * POST /admin/batch
//     */
//    @PostMapping("/batch")
//    public ResponseEntity<Map<String, Object>> batchOperation(
//            @RequestParam String operation,
//            @RequestParam(defaultValue = "false") boolean dryRun,
//            @RequestBody(required = false) Map<String, Object> params) {
//
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            switch (operation.toLowerCase()) {
//                case "reload_cars":
//                    carDataService.reloadData();
//                    result.put("message", "车型数据重新加载完成");
//                    break;
//                case "cleanup_users":
//                    result.put("message", "用户清理完成");
//                    break;
//                case "cleanup_tunes":
//                    result.put("message", "调校清理完成");
//                    break;
//                default:
//                    result.put("message", "未知操作: " + operation);
//                    result.put("status", "error");
//                    return ResponseEntity.badRequest().body(result);
//            }
//
//            result.put("operation", operation);
//            result.put("dryRun", dryRun);
//            result.put("params", params);
//            result.put("status", "success");
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Batch operation failed", e);
//            result.put("message", "批量操作失败: " + e.getMessage());
//            result.put("status", "error");
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
//
//    /**
//     * 获取系统统计信息
//     * GET /admin/stats
//     */
//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getSystemStats() {
//        Map<String, Object> stats = new HashMap<>();
//
//        try {
//            // 车型统计
//            stats.put("cars", Map.of(
//                "total", carDataService.getAllCars().size(),
//                "manufacturers", carDataService.getAllManufacturers().size(),
//                "dataVersion", carDataService.getDataInfo().get("version")
//            ));
//
//            // 用户统计
//            stats.put("users", Map.of(
//                "proPlayers", userService.getProPlayerCount()
//            ));
//
//            // 系统信息
//            stats.put("system", Map.of(
//                "javaVersion", System.getProperty("java.version"),
//                "osName", System.getProperty("os.name"),
//                "memoryUsage", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
//                "totalMemory", Runtime.getRuntime().totalMemory()
//            ));
//
//            return ResponseEntity.ok(stats);
//        } catch (Exception e) {
//            log.error("Failed to get system stats", e);
//            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
//        }
//    }
//}