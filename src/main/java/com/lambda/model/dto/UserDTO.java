package com.lambda.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lambda.util.CustomAuthoritySerializer;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements UserDetails, Serializable {

    private Long id;

    private UserProfileDTO userProfile;

    private SettingDTO setting;

    private String username;

    private String password;

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    @JsonSerialize(using = CustomAuthoritySerializer.class)
    private Set<GrantedAuthority> authorities;

    public UserDTO(String username, String password, boolean enabled,
                   boolean accountNonExpired, boolean accountNonLocked,
                   boolean credentialsNonExpired, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserDetails getBasicInfo() {
        return User.withUserDetails(this).build();
    }


}

