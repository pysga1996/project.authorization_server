package com.lambda.config.security;

import com.lambda.config.customization.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder() )
//                .withUser("john").password(passwordEncoder().encode("123")).roles("USER").and()
//                .withUser("tom").password(passwordEncoder().encode("111")).roles("ADMIN").and()
//                .withUser("user1").password(passwordEncoder().encode("pass")).roles("USER").and()
//                .withUser("admin").password(passwordEncoder().encode("nimda")).roles("ADMIN");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM lambda_user WHERE username =?")
                .authoritiesByUsernameQuery("SELECT lambda_user.username, role.authority FROM lambda_user INNER JOIN user_role ON lambda_user.id = user_role.user_id INNER JOIN role ON role.id = user_role.role_id where lambda_user.username =?")
                .passwordEncoder(passwordEncoder())
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
                .authorizeRequests().antMatchers("/", "/login", "/oauth/token", "/oauth/check_token", "/tokens/**").permitAll()
                .antMatchers("/client/test").access("permitAll()")
                .anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/login")
                .and().csrf().disable()
                .cors();
    }
}
