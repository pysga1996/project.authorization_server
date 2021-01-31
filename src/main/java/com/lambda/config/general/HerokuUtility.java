package com.lambda.config.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
@Profile("heroku")
public class HerokuUtility {

    private static final Logger LOG = LoggerFactory.getLogger(HerokuUtility.class);

    @PostConstruct
    public void herokuNotIdle(){
        LOG.debug("Ping heroku services");
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getForObject("https://lambda-authorization-server.herokuapp.com", String.class);
    }
}
