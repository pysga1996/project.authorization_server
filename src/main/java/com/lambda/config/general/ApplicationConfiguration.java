package com.lambda.config.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

    private final HandlerInterceptor localeChangeInterceptor;

    @Autowired
    public ApplicationConfiguration(HandlerInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
//                .allowedOrigins("https://climax-sound.netlify.com", "http://localhost:8080")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }
}
