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
     * 检查当前用户是否已登录
     */
    public static boolean isUserLoggedIn() {
        String userId = getCurrentUserId();
        return userId != null && !userId.isEmpty();
    }
}
