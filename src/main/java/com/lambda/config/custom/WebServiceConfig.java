package com.lambda.config.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurationSupport;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;

import java.util.List;

@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurationSupport implements WsConfigurer {

    private final PayloadLoggingInterceptor payloadLoggingInterceptor;

    private final PayloadValidatingInterceptor payloadValidatingInterceptor;

    private final XwsSecurityInterceptor xwsSecurityInterceptor;

    @Autowired
    public WebServiceConfig(PayloadLoggingInterceptor payloadLoggingInterceptor, PayloadValidatingInterceptor payloadValidatingInterceptor, XwsSecurityInterceptor xwsSecurityInterceptor) {
        this.payloadLoggingInterceptor = payloadLoggingInterceptor;
        this.payloadValidatingInterceptor = payloadValidatingInterceptor;
        this.xwsSecurityInterceptor = xwsSecurityInterceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(payloadLoggingInterceptor);
        interceptors.add(payloadValidatingInterceptor);
        interceptors.add(xwsSecurityInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<MethodArgumentResolver> argumentResolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<MethodReturnValueHandler> returnValueHandlers) {

    }
}
