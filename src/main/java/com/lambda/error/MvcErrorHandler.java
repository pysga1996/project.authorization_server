package com.lambda.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(annotations = {Controller.class})
@SuppressWarnings("deprecation")
public class MvcErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAllException(Exception ex, WebRequest request) throws Exception {
        // quá trình kiểm soat lỗi diễn ra ở đây
        if (ex instanceof AccessDeniedException) {
            throw ex;
        }
        ex.printStackTrace();
        return new ModelAndView("error");
    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView handleAllException(Exception ex, WebRequest request) {
//        // quá trình kiểm soat lỗi diễn ra ở đây
//        return new ModelAndView("error");
//    }
}
