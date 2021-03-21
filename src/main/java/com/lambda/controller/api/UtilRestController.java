package com.lambda.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/util")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true",
        exposedHeaders = {HttpHeaders.SET_COOKIE})
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
