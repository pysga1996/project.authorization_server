package com.lambda.model;

import com.lambda.dto.soap.user.SettingDTO;
import com.lambda.dto.soap.user.UserDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomUser extends User implements Serializable {

    private Long id;

    private String avatarUrl;

    private Setting setting;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserDTO toDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAvatarUrl(this.avatarUrl);
        userDTO.setId(this.id);
        userDTO.setUsername(this.getUsername());
        userDTO.setPassword(this.getPassword());
        userDTO.getAuthorities().addAll(this.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setId(this.setting.getId());
        settingDTO.setDarkMode(this.setting.getDarkMode());
        userDTO.setSetting(settingDTO);
        return userDTO;
    }
}
