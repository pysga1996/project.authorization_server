package com.lambda.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@SuppressWarnings("deprecation")
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oath2.resource-id}")
    private String RESOURCE_ID;

    private final TokenStore tokenStore;

    @Autowired
    public ResourceServerConfig(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and()
                .anonymous().authorities("ROLE_ANONYMOUS").key("anonymous").and()
//                .anonymous().disable() // don't enable this
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
        ;
    }
}
