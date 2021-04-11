package com.lambda.config.custom;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Log4j2
@RefreshScope
@Component
@Profile({"heroku", "poweredge"})
public class RemoteHostResolver implements HostResolver {

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
        String context = this.environment.getProperty("server.servlet.context-path", String.class, "/");
        if ("heroku".equals(activeProfile)) {
            ip = InetAddress.getLocalHost().getHostAddress();
            url = ip + context + path;
        } else {
            ip = InetAddress.getLocalHost().getHostAddress();
            int port = this.environment.getProperty("server.port", Integer.class, 8081);
            url = ip + ":" + port + context + path;
        }
        log.info("Current url: {}", url);
        return url;
    }
}
