package com.lambda.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    public ResponseEntity<Void> revokeToken(@PathVariable("tokenId") String tokenId) {
        boolean isTokenExisted = this.tokenServices.revokeToken(tokenId);
        if (isTokenExisted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    public static void main(String[] args) {
//        String token = "80d44fbe-35af-4c05-a9b8-ba9fa34d034a";
//        MessageDigest digest;
//        try {
//            digest = MessageDigest.getInstance("MD5");
//        }
//        catch (NoSuchAlgorithmException e) {
//            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
//        }
//
//        try {
//            byte[] bytes = digest.digest(token.getBytes("UTF-8"));
//            System.out.println(String.format("%032x", new BigInteger(1, bytes)));;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

}
