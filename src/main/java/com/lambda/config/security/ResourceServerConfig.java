package com.lambda.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@SuppressWarnings("deprecation")
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//	@Value("${security.oath2.resource-id}")
	private final String RESOURCE_ID = "document";

	private final TokenStore tokenStore;

	@Autowired
	public ResourceServerConfig(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).tokenStore(tokenStore).stateless(false);
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new RestAuthenticationFailureHandler();
	}

	@Bean
	RestAccessDeniedHandler accessDeniedHandler() {
		return new RestAccessDeniedHandler();
	}

	@Bean
	RestAuthenticationEntryPoint authenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.anonymous().disable()
		.authorizeRequests()
		.anyRequest().permitAll()
		.and()
		.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
		.authenticationEntryPoint(authenticationEntryPoint());
	}
}
