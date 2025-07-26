package com.forzatune.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.forzatune.backend.mapper")
@EnableCaching
public class ForzaTuneProBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForzaTuneProBackendApplication.class, args);
    }
} 