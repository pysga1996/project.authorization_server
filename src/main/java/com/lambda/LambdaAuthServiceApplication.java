package com.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.lambda",
        exclude = {ErrorMvcAutoConfiguration.class})
public class LambdaAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LambdaAuthServiceApplication.class, args);
    }

}
