package com.forzatune.backend.config;

import com.forzatune.backend.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 公开接口 - 无需认证
                .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/home/dashboard")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/cars/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/tunes/public/**")).permitAll()
                
                // 需要认证的接口
                .requestMatchers(new AntPathRequestMatcher("/tunes/upload")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/tunes/*/like")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/tunes/*/favorite")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/comments/**")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/teams/**")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/users/profile")).authenticated()
                
                // 其他接口默认需要认证
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 