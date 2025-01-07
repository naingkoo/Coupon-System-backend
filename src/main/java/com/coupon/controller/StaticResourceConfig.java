package com.coupon.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static images from the directory
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/");  // Path to your image directory

        registry.addResourceHandler("/business_images/**")
                .addResourceLocations("file:src/main/resources/static/business_images/");

        registry.addResourceHandler("/users_images/**")
                .addResourceLocations("file:src/main/resources/static/users_images/");

        registry.addResourceHandler("/QR_images/**")
                .addResourceLocations("file:src/main/resources/static/QR_images/");


    }
}
