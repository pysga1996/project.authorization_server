package com.lambda.config.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Log4j2
@Primary
@Component
public class CustomAccessDeniedHandler extends ErrorResponseProducer implements
    AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException ex) throws IOException {
        log.error(ex);
        if (request.getServletPath().startsWith("/api") || request.getServletPath()
            .startsWith("/oauth")) {
            super.produceErrorResponse(request, response, ex, HttpStatus.FORBIDDEN);
        } else {
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader(HttpHeaders.LOCATION, request.getContextPath() + "/error/403");
        }
    }
}
