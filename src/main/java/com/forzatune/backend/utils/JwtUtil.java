package com.forzatune.backend.utils;

import com.forzatune.backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:forzatune-pro-secret-key-2024-very-long-and-secure}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24小时（毫秒）
    private Long expiration;

    /**
     * 生成JWT token（基于用户对象）
     */
    public String generateToken(User user) {
        return generateToken(user.getId(), user.getEmail());
    }

    /**
     * 生成JWT token
     */
    public String generateToken(String userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证token并返回用户ID
     */
    public String validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.debug("Token已过期: {}", e.getMessage());
            return null;
        } catch (JwtException e) {
            log.debug("Token无效: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从token中提取用户ID
     */
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中提取邮箱
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否有效（布尔值版本）
     */
    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("Token已过期: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.debug("Token无效: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查token是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 解析token获取Claims
     */
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}