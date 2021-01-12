package com.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonComponent
public class User implements UserDetails, Serializable {

    private Long id;

    private String password;

    private String username;

    private Set<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String avatarUrl;

    private Setting setting;
}
