package com.lambda.config.general;

import java.net.InetAddress;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Log4j2
@RefreshScope
@Component
@Profile("default")
public class LocalHostResolver implements HostResolver {

    private final Environment environment;

    @Autowired
    public LocalHostResolver(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String resolveHost(String path) {
        String ip = InetAddress.getLoopbackAddress().getHostAddress();
        int port = this.environment.getProperty("server.port", Integer.class, 8081);
        String context = this.environment
            .getProperty("server.servlet.context-path", String.class, "/");
        String url = ip + ":" + port + context;
        log.info("Current url: {}", url);
        return url;
    }
}
