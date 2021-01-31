package com.lambda.config.custom;

import java.net.UnknownHostException;

public interface HostResolver {

    String resolveHost(String path) throws UnknownHostException;
}
