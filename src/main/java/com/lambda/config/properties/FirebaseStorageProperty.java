package com.lambda.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage.firebase")
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "firebase")
public class FirebaseStorageProperty {

    private String databaseUrl;

    private String storageBucket;

    private String credentials;
}
