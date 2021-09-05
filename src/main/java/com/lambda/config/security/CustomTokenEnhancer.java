package com.lambda.config.security;

import com.lambda.service.UserService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@RefreshScope
@Component("customTokenEnhancer")
@SuppressWarnings("deprecation")
public class CustomTokenEnhancer implements TokenEnhancer {

    private static final String USERNAME = "user_name";

    private static final String ISSUE_AT = "iat";

    private static final String ROLES = "roles";

    private static final String AUTHORITIES = "authorities";

    private final UserService userService;

    @Autowired
    public CustomTokenEnhancer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
        OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        String username = authentication.getName();
        additionalInfo.put(USERNAME, username);
        additionalInfo.put(ISSUE_AT, Instant.now().getEpochSecond());
        additionalInfo.put(AUTHORITIES, authentication.getAuthorities().stream().map(
            GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        List<String> roles = this.userService.getRoles(username);
        additionalInfo.put(ROLES, roles);
        Map<String, Object> shortInfo = this.userService.getUserShortInfo(username);
        additionalInfo.putAll(shortInfo);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
