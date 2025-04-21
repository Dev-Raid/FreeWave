package com.freewave.domain.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        // 클라이언트가 인증 정보를 포함한 요청을 보낼 수 있도록 설정
        configuration.setAllowCredentials(true);

        // 허용할 Origin 설정 (React 앱의 주소)
        configuration.addAllowedOrigin("http://localhost:3000");

        // 클라이언트가 접근할 수 있는 헤더를 명시적으로 추가
        configuration.addExposedHeader("Content-Type");
        configuration.addExposedHeader("Set-Cookie");

        // 모든 HTTP 메서드(GET, POST, PUT, DELETE 등)를 허용
        configuration.addAllowedMethod("*");

        // 모든 요청 헤더를 허용
        configuration.addAllowedHeader("*");

        configuration.addExposedHeader("Authorization");

        // 특정 URL 패턴에 대해 CORS 정책 등록
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}