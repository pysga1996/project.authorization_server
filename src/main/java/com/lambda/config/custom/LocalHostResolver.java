package com.lambda.config.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
@Profile("default")
public class LocalHostResolver implements HostResolver {

    private static final Logger logger = LogManager.getLogger(LocalHostResolver.class);

    private final Environment environment;

    @Autowired
    public LocalHostResolver(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String resolveHost(String path) {
        String ip = InetAddress.getLoopbackAddress().getHostAddress();
        int port = this.environment.getProperty("server.port", Integer.class, 8081);
        String url = ip + ":" + port;
        logger.info("Current url: {}", url);
        return url;
    }
}
