package com.lambda.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.error.ApiError;
import com.lambda.model.dto.ViewMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Log4j2
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final ObjectMapper objectMapper;

	@Autowired
	public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

		if (request.getServletPath().startsWith("/api/") || request.getServletPath().startsWith("/oauth/")) {
			log.error("Api login failure!", exception);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			ApiError res = new ApiError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
			OutputStream out = response.getOutputStream();
			this.objectMapper.writeValue(out, res);
			out.flush();
		} else {
			ViewMessage viewMessage = new ViewMessage(exception.getMessage(), false);
			log.error("Mvc login failure!", exception);
//			response.setStatus(HttpStatus.FOUND.value());
//			response.setHeader(HttpHeaders.LOCATION, "/lambda-auth/login.html");
			try {
				request.setAttribute("viewMessage", viewMessage);
				request.getRequestDispatcher("/login").forward(request, response);
			} catch (ServletException ex) {
				log.error("Failed to forward to login servlet1", ex);
			}
		}
	}

}
