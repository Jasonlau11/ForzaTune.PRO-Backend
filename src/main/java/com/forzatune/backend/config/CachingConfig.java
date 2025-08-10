package com.forzatune.backend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 首页数据缓存
        cacheManager.registerCustomCache("homeDashboard",
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .build());

        cacheManager.registerCustomCache("cars",
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .build());

        // 新增：为 "carDetails" 缓存添加配置
//        cacheManager.registerCustomCache("carDetails",
//                Caffeine.newBuilder()
//                        .initialCapacity(50)
//                        .maximumSize(200)
//                        .expireAfterWrite(2, TimeUnit.HOURS)
//                        .build());

        // 邮箱验证码缓存：10分钟有效
        cacheManager.registerCustomCache("emailVerification",
                Caffeine.newBuilder()
                        .initialCapacity(1000)
                        .maximumSize(10000)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .build());

        // 发送冷却缓存：60秒
        cacheManager.registerCustomCache("emailSendCooldown",
                Caffeine.newBuilder()
                        .initialCapacity(1000)
                        .maximumSize(10000)
                        .expireAfterWrite(60, TimeUnit.SECONDS)
                        .build());

        // 验证码失败计数锁定：15分钟
        cacheManager.registerCustomCache("emailCodeFailCount",
                Caffeine.newBuilder()
                        .initialCapacity(1000)
                        .maximumSize(10000)
                        .expireAfterWrite(15, TimeUnit.MINUTES)
                        .build());

        // 每日配额：24小时
        cacheManager.registerCustomCache("emailDailyQuota",
                Caffeine.newBuilder()
                        .initialCapacity(1000)
                        .maximumSize(20000)
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .build());

        return cacheManager;
    }
}