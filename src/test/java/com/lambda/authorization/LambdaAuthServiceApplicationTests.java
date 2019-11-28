package com.lambda.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(AuthorizationTestConfiguration.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class LambdaAuthServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
