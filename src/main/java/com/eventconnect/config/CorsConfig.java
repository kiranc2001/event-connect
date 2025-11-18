package com.eventconnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // All endpoints
                .allowedOrigins("http://localhost:3000")  // Exact frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // Methods you use
                .allowedHeaders("*")  // All headers
                .allowCredentials(true);  // Allows cookies (JSESSIONID)
    }
}