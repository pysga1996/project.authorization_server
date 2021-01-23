package com.lambda.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;

@Component
public class CustomPayloadLoggingInterceptor extends PayloadLoggingInterceptor {

    public CustomPayloadLoggingInterceptor() {
        this.setLoggerName("WsLogger");
    }
}
