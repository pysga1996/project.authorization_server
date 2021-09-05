package com.lambda.controller.mvc;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
@RequestMapping("")
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LogManager.getLogger(CustomErrorController.class);

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public String test(@RequestParam("x") Long x, Authentication authentication) {
        if (x == 0) {
            throw new RuntimeException("");
        }
        return "test";
    }

    @GetMapping("/error/{status}")
    public ModelAndView error(@PathVariable("status") String status) {
        return new ModelAndView("error/" + status);
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
            .getAttribute("javax.servlet.error.status_code");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
