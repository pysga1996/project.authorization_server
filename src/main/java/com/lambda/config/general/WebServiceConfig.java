package com.lambda.config.general;

import com.lambda.model.ws.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfig {

    @Bean
    public ObjectFactory objectFactory() {
        return new ObjectFactory();
    }
}
