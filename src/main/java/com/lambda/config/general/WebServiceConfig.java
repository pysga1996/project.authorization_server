package com.lambda.config.general;

import com.lambda.model.ws.ObjectFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class WebServiceConfig {

    @Bean
    public ObjectFactory objectFactory() {
        return new ObjectFactory();
    }
}
