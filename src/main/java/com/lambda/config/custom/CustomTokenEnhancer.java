package com.lambda.config.custom;

import com.lambda.model.dto.UserDTO;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
                additionalInfo.put("profile", optionalUserDTO.get().getUserProfile());
                additionalInfo.put("setting", optionalUserDTO.get().getSetting());
                additionalInfo.put("authorities", optionalUserDTO.get().getAuthorities());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        }
        return accessToken;
    }

}
