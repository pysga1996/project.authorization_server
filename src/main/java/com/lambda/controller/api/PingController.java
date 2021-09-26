package com.lambda.controller.api;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author thanhvt
 * @created 06/09/2021 - 12:20 SA
 * @project vengeance
 * @since 1.0
 **/
@Log4j2
@RestController
@RequestMapping("")
public class PingController {

    @RequestMapping(method = RequestMethod.HEAD, value = "/ping")
    public ResponseEntity<Void> ping(HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
