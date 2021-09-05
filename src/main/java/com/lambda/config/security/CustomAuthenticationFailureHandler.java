package com.lambda.config.security;

import com.lambda.model.dto.ViewMessage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CustomAuthenticationFailureHandler extends ErrorResponseProducer implements
    AuthenticationFailureHandler {

    @Autowired
    public CustomAuthenticationFailureHandler() {
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException ex)
        throws IOException, ServletException {

        if (request.getServletPath().startsWith("/api/") || request.getServletPath()
            .startsWith("/oauth/")) {
            super.produceErrorResponse(request, response, ex, HttpStatus.UNAUTHORIZED);
        } else {
            ViewMessage viewMessage = new ViewMessage(ex.getMessage(), false);
            log.error("Mvc login failure!", ex);
//			response.setStatus(HttpStatus.FOUND.value());
//			response.setHeader(HttpHeaders.LOCATION, "/lambda-auth/login.html");
            request.setAttribute("viewMessage", viewMessage);
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }

}
