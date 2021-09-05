package com.lambda.config.general;

import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Log4j2
@RefreshScope
@Component
@Profile("heroku")
public class HerokuUtility {

    @PostConstruct
    public void herokuNotIdle() {
        log.debug("Ping heroku services");
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getForObject("https://lambda-authorization-server.herokuapp.com", String.class);
    }
}
