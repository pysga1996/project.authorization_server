package com.lambda.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
		if (request.getServletPath().startsWith("/api/") || request.getServletPath().startsWith("/oauth/")) {
			response.setStatus(HttpStatus.FOUND.value());
			response.setHeader(HttpHeaders.LOCATION, "/lambda-auth/oauth/token");
		} else {
			response.setStatus(HttpStatus.FOUND.value());
			response.setHeader(HttpHeaders.LOCATION, "/lambda-auth/login.html");
		}
	}

}
