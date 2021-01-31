package com.lambda.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

import static com.lambda.constant.JdbcConstant.DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY;
import static com.lambda.constant.JdbcConstant.DEF_USERS_BY_USERNAME_FULL_QUERY;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder() )
//                .withUser("john").password(passwordEncoder().encode("123")).roles("USER").and()
//                .withUser("tom").password(passwordEncoder().encode("111")).roles("ADMIN").and()
//                .withUser("thanhvt").password(passwordEncoder().encode("1381996")).roles("USER").and()
//                .withUser("admin").password(passwordEncoder().encode("nimda")).roles("ADMIN");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(DEF_USERS_BY_USERNAME_FULL_QUERY)
//                .authoritiesByUsernameQuery("SELECT user.username, role.authority FROM user INNER JOIN user_role ON user.id = user_role.user_id INNER JOIN role ON role.id = user_role.role_id where username =?")
                .groupAuthoritiesByUsername(DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY)
                .passwordEncoder(passwordEncoder)
                .getUserDetailsService().setEnableAuthorities(false);
        ;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.requiresChannel()
                // Heroku https config
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure().and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/oauth/token", "/oauth/check_token", "/tokens/**", "/ws/**").permitAll()
                .antMatchers("/com/lambda/client/**").access("permitAll()")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .cors()
                .and().rememberMe(httpSecurityRememberMeConfigurer ->
                httpSecurityRememberMeConfigurer.key("lambda").alwaysRemember(true));
    }
}
