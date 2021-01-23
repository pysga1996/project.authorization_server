package com.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lambda")
public class LambdaAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LambdaAuthServiceApplication.class, args);
    }


}
