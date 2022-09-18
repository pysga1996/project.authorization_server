package com.lambda.config.security;

import static com.lambda.constant.JdbcConstant.DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY;
import static com.lambda.constant.JdbcConstant.DEF_USERS_BY_USERNAME_FULL_QUERY;

import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Order(2)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, order = Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final OpaqueTokenIntrospector opaqueTokenIntrospector;
    private final Environment env;
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Autowired
    public SecurityConfig(DataSource dataSource, PasswordEncoder passwordEncoder,
        AuthenticationEntryPoint authenticationEntryPoint,
        AccessDeniedHandler accessDeniedHandler,
        AuthenticationFailureHandler authenticationFailureHandler,
        LogoutSuccessHandler logoutSuccessHandler, OpaqueTokenIntrospector opaqueTokenIntrospector,
        Environment env) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
        this.env = env;
    }

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(DEF_USERS_BY_USERNAME_FULL_QUERY)
//                .authoritiesByUsernameQuery("SELECT user.username, role.authority FROM user INNER JOIN user_role ON user.id = user_role.user_id INNER JOIN role ON role.id = user_role.role_id where username =?")
            .groupAuthoritiesByUsername(DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY)
            .passwordEncoder(passwordEncoder)
            .getUserDetailsService().setEnableAuthorities(false)
        ;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/icon/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.requiresChannel()
            // Heroku https config
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure().and()
            .anonymous().and()
//                .anonymous().disable() // don't enable this
            .authorizeRequests()
            .anyRequest().permitAll().and()
            .csrf().disable()
            .formLogin()
            .loginPage("/login.html")
            .usernameParameter("username")
            .passwordParameter("password")
            .loginProcessingUrl("/perform_login")
            .defaultSuccessUrl("/homepage.html", true)
            .failureUrl("/login.html?error=true")
            .failureHandler(authenticationFailureHandler)
            .and()
            .logout()
            .logoutUrl("/perform_logout")
//                .logoutSuccessUrl("/homepage.html")
            .deleteCookies("SESSIONID")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .logoutSuccessHandler(logoutSuccessHandler)
            .and()
            .rememberMe()
            .key("LambdaRememberMe").tokenValiditySeconds(86400)
            .rememberMeParameter("remember")
            .rememberMeCookieName("remember-cookie")
            .and()
            .cors()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and()
            .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
                bearerTokenResolver.setAllowUriQueryParameter(true);
                if (CloudPlatform.HEROKU.isActive(this.env) || !Arrays
                    .asList(this.env.getActiveProfiles()).contains("default")) {
                    httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer ->
                        jwtConfigurer.jwkSetUri(this.jwkSetUri));
                } else {
                    httpSecurityOAuth2ResourceServerConfigurer.opaqueToken(opaqueTokenConfigurer ->
                        opaqueTokenConfigurer.introspector(opaqueTokenIntrospector));
                }
                httpSecurityOAuth2ResourceServerConfigurer.bearerTokenResolver(bearerTokenResolver);
            })
            .headers()
            .frameOptions().sameOrigin()
            .httpStrictTransportSecurity().disable()
            .and()
        ;
    }
}
