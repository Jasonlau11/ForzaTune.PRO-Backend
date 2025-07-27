package com.forzatune.backend.filter;

import com.forzatune.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${auth.dev-mode:false}")
    private boolean devMode;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        // 开发模式：跳过验证
        if (devMode) {
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("dev_user", null, new ArrayList<>())
            );
            filterChain.doFilter(request, response);
            return;
        }

        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

        // 检查是否有Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            
            // 验证token并提取用户信息
            if (jwtUtil.validateToken(token)) {
                userId = jwtUtil.getUserIdFromToken(token);
            }
        }

        // 如果token有效且当前没有认证信息，设置认证
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 创建认证对象（简化实现，不查询数据库）
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}