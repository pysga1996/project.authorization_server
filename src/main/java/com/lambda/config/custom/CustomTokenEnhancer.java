package com.lambda.config.custom;

import com.lambda.model.dto.SettingDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("deprecation")
public class CustomTokenEnhancer implements TokenEnhancer {

    private final UserService userService;

    @Autowired
    public CustomTokenEnhancer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            Optional<UserDTO> optionalUserDTO = this.userService
                    .findByUsername(((UserDetails) principal).getUsername());
            final Map<String, Object> additionalInfo = new TreeMap<>();
            if (optionalUserDTO.isPresent()) {
                additionalInfo.put("id", optionalUserDTO.get().getId());
                if (optionalUserDTO.get().getUserProfile() == null) {
                    optionalUserDTO.get().setUserProfile(new UserProfileDTO());
                }
                if (optionalUserDTO.get().getSetting() == null) {
                    optionalUserDTO.get().setSetting(new SettingDTO());
                }
                additionalInfo.put("profile", optionalUserDTO.get().getUserProfile());
                additionalInfo.put("setting", optionalUserDTO.get().getSetting());
                Set<String> authorities = optionalUserDTO.get().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                additionalInfo.put("authorities", authorities);
                Map<String, Boolean> otherInfo = new HashMap<>();
                otherInfo.put("enabled", optionalUserDTO.get().isEnabled());
                otherInfo.put("accountNonLocked", optionalUserDTO.get().isAccountNonLocked());
                otherInfo.put("accountNonExpired", optionalUserDTO.get().isAccountNonExpired());
                otherInfo.put("credentialsNonExpired", optionalUserDTO.get().isCredentialsNonExpired());
                additionalInfo.put("other", otherInfo);
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        }
        return accessToken;
    }

}
