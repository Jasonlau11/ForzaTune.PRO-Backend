package com.forzatune.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * HTTP 请求信息工具类
 * <p>
 * 提供静态方法，用于在应用的任何地方方便地获取当前请求的上下文信息。
 * </p>
 */
@Component // 改为 Spring 组件以注入配置
public final class RequestUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DEV_USER_ID_HEADER = "X-User-ID"; // 开发环境使用的自定义头
    private static final String DEV_XBOX_ID_HEADER = "X-Xbox-ID"; // 开发环境使用的Xbox ID头
    private static final String DEV_IS_PRO_HEADER = "X-Is-Pro"; // 开发环境使用的Pro玩家状态头
    private static final String GAME_CATEGORY_HEADER = "X-Game-Category"; // 游戏分类头

    private static boolean isDevMode = false;

    /**
     * 通过 @Value 注解从 application.yml/properties 读取开发模式配置
     * @param devMode 对应配置中的 auth.dev-mode: true/false
     */
    @Value("${auth.dev-mode:false}")
    public void setDevMode(boolean devMode) {
        RequestUtils.isDevMode = devMode;
    }

    // ... (其他方法如 getCurrentRequest, getHeader, getToken 保持不变) ...
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
    public static String getHeader(String headerName) {
        HttpServletRequest request = getCurrentRequest();
        return request != null ? request.getHeader(headerName) : null;
    }
    public static String getToken() {
        String authHeader = getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }


    /**
     * 获取当前登录用户的ID。
     * <p>
     * 在开发模式 (auth.dev-mode=true) 下，会尝试从 "X-User-ID" 请求头直接获取ID。
     * 在生产模式下，会从 Spring Security 上下文中安全地获取ID。
     * </p>
     * @return 当前用户的ID，如果找不到则返回空字符串。
     */
    public static String getCurrentUserId() {
        // 如果是开发模式，优先从请求头获取，用于快速调试
        if (isDevMode) {
            String devUserId = getHeader(DEV_USER_ID_HEADER);
            if (devUserId != null && !devUserId.isEmpty()) {
                return devUserId;
            }
            // 开发模式下如果没有提供X-User-ID头，返回默认用户ID
            return "dev_user";
        }

        // 生产模式：从 Spring Security 上下文中获取用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "";
        }

        Object principal = authentication.getPrincipal();

        // JWT认证后，principal就是userId字符串
        if (principal instanceof String) {
            return principal.toString();
        }

        return "";
    }

    /**
     * 获取当前登录用户的Xbox ID。
     * <p>
     * 在开发模式 (auth.dev-mode=true) 下，会尝试从 "X-Xbox-ID" 请求头直接获取Xbox ID。
     * 在生产模式下，会从 Spring Security 上下文中获取用户信息，然后查询数据库获取Xbox ID。
     * </p>
     * @return 当前用户的Xbox ID，如果找不到则返回空字符串。
     */
    public static String getCurrentUserXboxId() {
        // 如果是开发模式，优先从请求头获取，用于快速调试
        if (isDevMode) {
            String devXboxId = getHeader(DEV_XBOX_ID_HEADER);
            if (devXboxId != null && !devXboxId.isEmpty()) {
                return devXboxId;
            }
            // 开发模式下如果没有提供X-Xbox-ID头，返回默认Xbox ID
            return "dev_xbox_user";
        }

        // 生产模式：从 Spring Security 上下文中获取用户ID，然后查询数据库
        String userId = getCurrentUserId();
        if (userId == null || userId.isEmpty()) {
            return "";
        }

        // TODO: 在生产环境中，这里需要注入UserService来查询用户的Xbox ID
        // 目前返回空字符串，表示需要实现数据库查询逻辑
        return "";
    }

    /**
     * 获取当前登录用户的Pro玩家状态。
     * <p>
     * 在开发模式 (auth.dev-mode=true) 下，会尝试从 "X-Is-Pro" 请求头直接获取Pro状态。
     * 在生产模式下，会从 Spring Security 上下文中获取用户信息，然后查询数据库获取Pro状态。
     * </p>
     * @return 当前用户的Pro玩家状态，如果找不到则返回false。
     */
    public static boolean getCurrentUserIsPro() {
        // 如果是开发模式，优先从请求头获取，用于快速调试
        if (isDevMode) {
            String devIsPro = getHeader(DEV_IS_PRO_HEADER);
            if (devIsPro != null && !devIsPro.isEmpty()) {
                return "true".equalsIgnoreCase(devIsPro);
            }
            // 开发模式下如果没有提供X-Is-Pro头，返回默认值
            return false;
        }

        // 生产模式：从 Spring Security 上下文中获取用户ID，然后查询数据库
        String userId = getCurrentUserId();
        if (userId == null || userId.isEmpty()) {
            return false;
        }

        // TODO: 在生产环境中，这里需要注入UserService来查询用户的Pro状态
        // 目前返回false，表示需要实现数据库查询逻辑
        return false;
    }

    /**
     * 获取当前请求的游戏分类。
     * <p>
     * 从 "X-Game-Category" 请求头获取游戏分类信息。
     * 如果请求头中没有提供，则返回默认值 "fh5"。
     * </p>
     * @return 当前请求的游戏分类，默认为 "fh5"。
     */
    public static String getCurrentGameCategory() {
        String gameCategory = getHeader(GAME_CATEGORY_HEADER);
        if (gameCategory != null && !gameCategory.isEmpty()) {
            return gameCategory;
        }
        // 如果没有提供游戏分类头，返回默认值
        return "fh5";
    }

    /**
     * 检查当前用户是否已登录
     */
    public static boolean isUserLoggedIn() {
        String userId = getCurrentUserId();
        return userId != null && !userId.isEmpty();
    }
}
