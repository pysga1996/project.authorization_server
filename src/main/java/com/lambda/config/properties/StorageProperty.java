package com.lambda.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage")
public class StorageProperty {

    enum StorageType {
        LOCAL, CLOUDINARY, FIREBASE
    }


    private StorageType storageType;

    private String temp;
}
