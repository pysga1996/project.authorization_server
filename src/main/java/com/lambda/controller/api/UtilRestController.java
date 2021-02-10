package com.lambda.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UtilRestController {

    @RequestMapping(value = "/ping", method = {RequestMethod.HEAD})
    public ResponseEntity<Void> ping() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public ResponseEntity<Void> test(@RequestParam("x") Long x) {
        if (x == 0) {
            throw new RuntimeException("");
        }
        return ResponseEntity.ok().build();
    }
}
