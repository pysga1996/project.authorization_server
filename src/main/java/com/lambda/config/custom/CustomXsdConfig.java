package com.lambda.config.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
public class CustomXsdConfig {

    @Bean
    public XsdSchema usersSchema(Resource usersResource) {
        return new SimpleXsdSchema(usersResource);
    }

    @Bean
    public XsdSchema clientsSchema(Resource clientsResource) {
        return new SimpleXsdSchema(clientsResource);
    }
}
