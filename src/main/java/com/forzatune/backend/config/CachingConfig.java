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

        return cacheManager;
    }
}