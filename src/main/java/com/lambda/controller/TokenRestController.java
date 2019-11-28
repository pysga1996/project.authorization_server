package com.lambda.controller;

import com.lambda.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
@SuppressWarnings("deprecation")
public class TokenRestController {
    private ConsumerTokenServices tokenServices;

    @Autowired
    public void setTokenServices(ConsumerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @PostMapping("/revoke/{tokenId:.*}")
    public ResponseEntity<ApiResponse> revokeToken(@PathVariable String tokenId) {
        try {
            tokenServices.revokeToken(tokenId);
            return new ResponseEntity<>(new ApiResponse(tokenId), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
