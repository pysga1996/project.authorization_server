package com.lambda.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SpringPlainTextPasswordValidationCallbackHandler;

@Component
public class CustomSecurityInterceptor extends XwsSecurityInterceptor {

    @Autowired
    public CustomSecurityInterceptor(@Lazy AuthenticationManager authenticationManager) {
        SpringPlainTextPasswordValidationCallbackHandler authenticationHandler
                = new SpringPlainTextPasswordValidationCallbackHandler();
        authenticationHandler.setAuthenticationManager(authenticationManager);
        this.setCallbackHandler(authenticationHandler);
        this.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
    }

//    public CustomSecurityInterceptor() {
//        SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
//        callbackHandler.setUsersMap(Collections.singletonMap("thanhvt", "123456"));
//        this.setCallbackHandler(callbackHandler);
//        this.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
//    }
}
