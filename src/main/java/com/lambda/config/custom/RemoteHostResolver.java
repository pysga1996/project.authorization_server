package com.lambda.config.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Profile({"heroku", "poweredge"})
public class RemoteHostResolver implements HostResolver {

    private static final Logger logger = LogManager.getLogger(RemoteHostResolver.class);

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final Environment environment;

    @Autowired
    public RemoteHostResolver(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String resolveHost(String path) throws UnknownHostException {
        String ip;
        String url;
        if ("heroku".equals(activeProfile)) {
            ip = InetAddress.getLocalHost().getHostAddress();
            url = ip + path;
        } else {
            ip = InetAddress.getLocalHost().getHostAddress();
            int port = this.environment.getProperty("server.port", Integer.class, 8081);
            url = ip + ":" + port + path;
        }
        logger.info("Current url: {}", url);
        return url;
    }
}
