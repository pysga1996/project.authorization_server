package com.lambda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    public UserController(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.jdbcUserDetailsManager = (JdbcUserDetailsManager)
                authenticationManagerBuilder.getDefaultUserDetailsService();
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@RequestBody UserDetails userDetails) {
        jdbcUserDetailsManager.createUser(userDetails);
        return ResponseEntity.ok().build();
    }
}
