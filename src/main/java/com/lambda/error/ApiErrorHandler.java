package com.lambda.error;

import com.lambda.config.security.ErrorResponseProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

@Log4j2
@RestControllerAdvice(annotations = {RestController.class})
@SuppressWarnings("deprecation")
public class ApiErrorHandler extends ErrorResponseProducer {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAllException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        log.error("Exception: {}, user: {}", ex, request.getRemoteUser());
        return new ApiError(9999, ex.getLocalizedMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleBusinessException(BusinessException ex, WebRequest request) {
        log.error("Exception: {}, user: {}", ex, request.getRemoteUser());
        return new ApiError(ex.getCode(), ex.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        log.error("Exception: {}, user: {}", ex, request.getRemoteUser());
        return new ApiError(2000, ex.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidTokenException(InvalidBearerTokenException ex,
        WebRequest request) {
        log.error("Exception: {}, user: {}", ex, request.getRemoteUser());
        return new ApiError(2000, ex.getLocalizedMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiError handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error("Exception: {}, user: {}", ex, request.getRemoteUser());
        return new ApiError(1500, ex.getLocalizedMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<byte[]> handleHttpClientException(HttpClientErrorException ex, WebRequest request) {
        log.error("Exception: {}, user: {}", ex, request.getRemoteUser());
        return new ResponseEntity<>(ex.getResponseBodyAsByteArray(), HttpStatus.valueOf(ex.getRawStatusCode()));
    }

}
