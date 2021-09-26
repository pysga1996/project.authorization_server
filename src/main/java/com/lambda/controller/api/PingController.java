package com.lambda.controller.api;

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
@RestController
@RequestMapping("")
public class PingController {

    @RequestMapping(method = RequestMethod.HEAD, value = "/ping")
    public ResponseEntity<Void> ping() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
