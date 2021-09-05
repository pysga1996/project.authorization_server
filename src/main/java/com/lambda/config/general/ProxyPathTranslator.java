package com.lambda.config.general;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(200)
public class ProxyPathTranslator implements Filter {

    public String translate(HttpServletRequest request, String path) {
        String proxyPath = request.getHeader("Proxy-Path");
        path = path.startsWith("/") ? path : "/" + path;
        if (proxyPath != null) {
            return proxyPath + path;
        } else {
            String ctxPath = request.getContextPath();
            return ctxPath + path;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        if (!String.valueOf(httpServletResponse.getStatus()).startsWith("3")) {
//            chain.doFilter(httpServletRequest, httpServletResponse);
//        }
//        String host = httpServletRequest.getRemoteHost();
//        String location = httpServletResponse.getHeader(HttpHeaders.LOCATION);
//        String proxyPath = httpServletResponse.getHeader("Proxy-Path");
//        if (location != null) {
//            URL aURL = new URL(location);
//            if (aURL.getHost().equals(httpServletRequest.getRemoteHost())) {
//                String path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
//                httpServletResponse.setHeader(HttpHeaders.LOCATION, host + proxyPath + path);
//            }
//        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }
}
