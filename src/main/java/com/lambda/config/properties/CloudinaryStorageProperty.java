package com.lambda.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage.cloudinary")
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "cloudinary")
public class CloudinaryStorageProperty {

    private String url;
}
