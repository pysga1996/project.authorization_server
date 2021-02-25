package com.lambda.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lambda.model.domain.Group;
import com.lambda.util.CustomAuthoritySerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO implements UserDetails, Serializable {

    private Long id;

    private UserProfileDTO userProfile;

    private SettingDTO setting;

    @NotBlank(message = "{validation.username.notBlank}")
    private String username;

    @Size(min = 6, max = 15)
    @NotBlank(message = "{validation.password.notBlank}")
    private String password;

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private Set<Group> groupList;

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

    public UserDTO(Long id, UserProfileDTO userProfile, SettingDTO setting,
                   String username, String password,
                   boolean enabled, boolean accountNonExpired, boolean accountNonLocked,
                   boolean credentialsNonExpired, Set<GrantedAuthority> authorities) {
        this.id = id;
        this.userProfile = userProfile;
        this.setting = setting;
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

