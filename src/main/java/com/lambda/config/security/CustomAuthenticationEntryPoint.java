package com.lambda.config.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Log4j2
@Primary
@Component
public class CustomAuthenticationEntryPoint extends ErrorResponseProducer implements
    AuthenticationEntryPoint {

    @Autowired
    public CustomAuthenticationEntryPoint() {
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException ex) throws IOException {
        log.error(ex);
        if (request.getServletPath().startsWith("/api/") || request.getServletPath()
            .startsWith("/oauth")) {
            super.produceErrorResponse(request, response, ex, HttpStatus.UNAUTHORIZED);
        } else {
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader(HttpHeaders.LOCATION, request.getContextPath() + "/login.html");
        }
    }

}
