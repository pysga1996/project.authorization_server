package com.lambda.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Log4j2
@RefreshScope
@Component
@Primary
@SuppressWarnings("deprecation")
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final ObjectMapper objectMapper;

    private final TokenStore tokenStore;

    @Autowired
    public CustomOpaqueTokenIntrospector(ObjectMapper objectMapper, TokenStore tokenStore) {
        this.objectMapper = objectMapper;
        this.tokenStore = tokenStore;
    }

    @SuppressWarnings("unchecked")
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(token);
        String username = oAuth2Authentication.getName();
        Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
        Map<String, Object> map = this.objectMapper.convertValue(oAuth2Authentication.getUserAuthentication().getPrincipal(), Map.class);
        return new DefaultOAuth2AuthenticatedPrincipal(username, map, authorities);
    }

}
