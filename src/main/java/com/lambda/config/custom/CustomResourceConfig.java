package com.lambda.config.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class CustomResourceConfig {

    @Bean
    public Resource usersResource() {
        return new ClassPathResource("xsd/user.xsd");
    }

    @Bean
    public Resource clientsResource() {
        return new ClassPathResource("xsd/client.xsd");
    }
}
