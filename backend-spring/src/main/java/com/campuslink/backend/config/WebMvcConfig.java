package com.campuslink.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://10.0.2.2:8080",
                        "http://localhost:8081"
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowCredentials(true);
    }


    // ⭐⭐⭐ 여기 추가 (정적 리소스 핸들링)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        //   URL           → 서버 폴더 경로

        // 예: /uploads/chat/170123123.jpg  
        //     👉 실제 파일은 서버 프로젝트 /uploads/chat/170123123.jpg 위치
    }
}
