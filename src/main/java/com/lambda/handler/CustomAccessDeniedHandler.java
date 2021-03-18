package com.lambda.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.error.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
	    if (request.getServletPath().startsWith("/api")) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			ApiError res = new ApiError(HttpStatus.FORBIDDEN.value(),
					accessDeniedException.getMessage());
			OutputStream out = response.getOutputStream();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(out, res);
			out.flush();
		} else {
			response.setStatus(HttpStatus.FOUND.value());
			response.setHeader(HttpHeaders.LOCATION, request.getContextPath() + "/error/403");
		}
	}
}
