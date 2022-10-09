package com.lambda.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * @author thanhvt
 * @created 27/04/2021 - 7:54 CH
 * @project vengeance
 * @since 1.0
 **/
@Log4j2
public abstract class ErrorResponseProducer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("HH:mm:ss dd/MM/yyyy")
        .withZone(ZoneId.systemDefault());

    /**
     * @param httpServletRequest  request
     * @param httpServletResponse response
     * @param ex                  error
     * @param httpStatus          status of response to set
     * @throws IOException exception if cannot get writer
     */
    protected void produceErrorResponse(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Exception ex, HttpStatus httpStatus)
        throws IOException {
        log.error("Error on URI: {}", httpServletRequest.getRequestURI(), ex);
        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", 9999);
            jsonObject.put("message", ex.getLocalizedMessage());
            jsonObject.put("servletPath", httpServletRequest.getServletPath());
            jsonObject.put("contextPath", httpServletRequest.getServletPath());
            jsonObject.put("pathInfo", httpServletRequest.getPathInfo());
            jsonObject.put("translatedPath", httpServletRequest.getPathTranslated());
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
                sb.append(stackTraceElement.toString()).append("\n");
            }
            jsonObject.put("detail", sb.toString());
            jsonObject.put("timestamp", FORMATTER.format(Instant.now()));
        } catch (JSONException exception) {
            log.error(ex);
        }
        out.print(jsonObject);
        out.flush();
    }

}
