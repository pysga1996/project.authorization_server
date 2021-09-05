package com.lambda.error;

import com.lambda.model.dto.ViewMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@ControllerAdvice(annotations = {Controller.class})
public class MvcErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAllException(Exception ex, WebRequest request) throws Exception {
        // quá trình kiểm soat lỗi diễn ra ở đây
        if (ex instanceof AccessDeniedException) {
            throw ex;
        }
        log.error(ex);
        StringBuilder sb = new StringBuilder(ex.getMessage()).append("\n");
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            sb.append(stackTraceElement).append("\n");
        }
        return new ModelAndView("error/500", "viewMessage",
            new ViewMessage(ex.getMessage(), false));
    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView handleAllException(Exception ex, WebRequest request) {
//        // quá trình kiểm soat lỗi diễn ra ở đây
//        return new ModelAndView("error");
//    }
}
