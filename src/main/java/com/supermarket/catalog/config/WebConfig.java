package com.supermarket.catalog.config;

import com.supermarket.catalog.validation.HeaderUserValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final HeaderUserValidator headerUserValidator;

    public WebConfig(HeaderUserValidator headerUserValidator) {
        this.headerUserValidator = headerUserValidator;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerUserValidator)
                .addPathPatterns("/products/**");
    }
}