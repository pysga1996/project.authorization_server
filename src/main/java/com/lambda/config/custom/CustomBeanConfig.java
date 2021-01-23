package com.lambda.config.custom;

import com.lambda.constant.WebServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.XsdSchema;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@DependsOn({"dataSource"})
@SuppressWarnings("deprecation")
public class CustomBeanConfig {

    private final DataSource dataSource;

    private final UserDetailsService userDetailsService;

    @Autowired
    public CustomBeanConfig(DataSource dataSource,
                            @Lazy @Qualifier("userDaoImpl") UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//        jdbcUserDetailsManager.setEnableAuthorities(false);
//        jdbcUserDetailsManager.setEnableGroups(true);
//        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled,  FROM user WHERE username =?");
//        return jdbcUserDetailsManager;
//    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:static/i18n/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean
    public HandlerInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    @Primary
    public ConsumerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices tokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("lambda", userDetailsService, persistentTokenRepository());
    }

//    @Bean
//    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() {
//        return new RememberMeAuthenticationFilter(authenticationManager, tokenBasedRememberMeServices());
//    }
//
//    @Bean
//    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
//        return new RememberMeAuthenticationProvider("lambda");
//    }

    @Bean
    public JdbcOperations jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Primary
    public ServletRegistrationBean<HttpServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }


    @Bean(name = "users")
    public Wsdl11Definition usersWsdl11Definition(XsdSchema usersSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("UserPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(WebServiceConstant.USERS_NAMESPACE);
        wsdl11Definition.setServiceName("UserService");
        wsdl11Definition.setResponseSuffix("Response");
        wsdl11Definition.setRequestSuffix("Request");
        wsdl11Definition.setSchema(usersSchema);
        return wsdl11Definition;
    }

    @Bean(name = "clients")
    public Wsdl11Definition clientsWsdl11Definition(XsdSchema clientsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClientPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(WebServiceConstant.CLIENTS_NAMESPACE);
        wsdl11Definition.setServiceName("ClientService");
        wsdl11Definition.setResponseSuffix("Response");
        wsdl11Definition.setRequestSuffix("Request");
        wsdl11Definition.setSchema(clientsSchema);
        Properties soapActions = new Properties();
        soapActions.setProperty("getClientList", "http://www.lambda.authorization/clients/list");
        soapActions.setProperty("createClient", "http://www.lambda.authorization/clients/create");
        wsdl11Definition.setSoapActions(soapActions);
        return wsdl11Definition;
    }
}
