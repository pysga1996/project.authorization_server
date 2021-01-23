package com.lambda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@SuppressWarnings("deprecation")
public class TokenRestController {

    private final ConsumerTokenServices tokenServices;

    @Autowired
    public TokenRestController(ConsumerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @DeleteMapping("/revoke/{tokenId:.*}")
    public ResponseEntity<Void> revokeToken(@PathVariable String tokenId) {
        try {
            boolean isTokenExisted = tokenServices.revokeToken(tokenId);
            if (isTokenExisted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
