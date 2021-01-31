package com.lambda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UtilRestController {

    @RequestMapping(value = "/ping", method = {RequestMethod.TRACE})
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }
}
