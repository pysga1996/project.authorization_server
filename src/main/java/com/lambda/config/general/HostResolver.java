package com.lambda.config.general;

import java.net.UnknownHostException;

public interface HostResolver {

    String resolveHost(String path) throws UnknownHostException;
}
