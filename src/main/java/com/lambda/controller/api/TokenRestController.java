package com.lambda.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/token")
@SuppressWarnings("deprecation")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true",
        exposedHeaders = {HttpHeaders.SET_COOKIE})
public class TokenRestController {

    private final ConsumerTokenServices tokenServices;

    @Autowired
    public TokenRestController(ConsumerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @DeleteMapping("/revoke/{tokenId:.*}")
    public ResponseEntity<Void> revokeToken(@PathVariable("tokenId") String tokenId) {
        boolean isTokenExisted = this.tokenServices.revokeToken(tokenId);
        if (isTokenExisted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
