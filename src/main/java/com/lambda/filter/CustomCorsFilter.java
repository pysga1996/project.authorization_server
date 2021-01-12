package com.lambda.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@AllArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String[] methodsAllowed = {HttpMethod.POST.name(), HttpMethod.GET.name(),
                HttpMethod.OPTIONS.name(), HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(), HttpMethod.DELETE.name()};
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                String.join(",", methodsAllowed));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");

        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
