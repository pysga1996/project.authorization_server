package com.lambda.config.security;

import com.lambda.error.ApiError;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author thanhvt
 * @created 05/06/2021 - 11:56 SA
 * @project vengeance
 * @since 1.0
 **/
@Log4j2
@Primary
@Component
@SuppressWarnings("deprecation")
public class CustomOAuth2ExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("HH:mm:ss dd/MM/yyyy")
        .withZone(ZoneId.systemDefault());
    private final MessageSource messageSource;

    @Autowired
    public CustomOAuth2ExceptionTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error(e);
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
        OAuth2Exception body = responseEntity.getBody();
        if (body == null) {
            body = new OAuth2Exception(e.getLocalizedMessage(), e);
        }
        HttpStatus statusCode = responseEntity.getStatusCode();
        body.addAdditionalInformation("code", body.getOAuth2ErrorCode().toUpperCase());
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append(stackTraceElement.toString()).append("\n");
        }
        body.addAdditionalInformation("detail", sb.toString());
        body.addAdditionalInformation("message", this.getI18NKey(body));
        body.addAdditionalInformation("timestamp", FORMATTER.format(Instant.now()));
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(responseEntity.getHeaders().toSingleValueMap());
        // do something with header or response
        return new ResponseEntity<>(body, headers, statusCode);
    }

    private String getI18NKey(OAuth2Exception oAuth2Exception) {
        String code = oAuth2Exception.getOAuth2ErrorCode();
        String messageKey = "validation.unknown_error";
        switch (code) {
            case OAuth2Exception.INVALID_GRANT:
                messageKey = "validation.login.invalid_username_password";
                break;
            case OAuth2Exception.INVALID_CLIENT:
                messageKey = "validation.login.invalid_client";
                break;
            case OAuth2Exception.INVALID_REQUEST:
                messageKey = "validation.login.invalid_request";
                break;
            case OAuth2Exception.INVALID_SCOPE:
                messageKey = "validation.login.invalid_scope";
                break;
            case OAuth2Exception.INVALID_TOKEN:
                messageKey = "validation.login.invalid_token";
                break;
            default:
                break;
        }
        return this.messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}
