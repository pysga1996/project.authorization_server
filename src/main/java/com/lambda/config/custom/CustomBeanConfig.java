package com.lambda.config.custom;

import com.cloudinary.Cloudinary;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.lambda.error.FileStorageException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@RefreshScope
@Configuration
@DependsOn({"dataSource"})
@SuppressWarnings("deprecation")
public class CustomBeanConfig {

    private final DataSource dataSource;

    private final UserDetailsService userDetailsService;

    @Value("${storage.cloudinary.url}")
    private String cloudinaryUrl;

    @Value("${storage.firebase.database-url}")
    private String firebaseDatabaseUrl;

    @Value("${storage.firebase.storage-bucket}")
    private String firebaseStorageBucket;

    @Value("${storage.firebase.credentials}")
    private String firebaseCredentials;

    @Value("${spring.profiles.active:Default}")
    private String activeProfile;

    @Value("${custom.http-port}")
    private Integer httpPort;

    @Value("${custom.https-port}")
    private Integer httpsPort;

    @Value("${custom.security-policy}")
    private String securityPolicy;

    @Value("${custom.connector-scheme}")
    private String connectorScheme;

    private final TokenEnhancer tokenEnhancer;

    private final JwtAccessTokenConverter jwtTokenConverter;

    private final Environment env;

    @Autowired
    public CustomBeanConfig(DataSource dataSource,
        @Lazy @Qualifier("userDaoImpl") UserDetailsService userDetailsService,
        @Qualifier("customTokenEnhancer") TokenEnhancer tokenEnhancer,
        @Qualifier("customJwtTokenConverter") JwtAccessTokenConverter jwtTokenConverter,
        Environment env) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.tokenEnhancer = tokenEnhancer;
        this.jwtTokenConverter = jwtTokenConverter;
        this.env = env;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//        jdbcUserDetailsManager.setEnableAuthorities(false);
//        jdbcUserDetailsManager.setEnableGroups(true);
//        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled,  FROM user WHERE username =?");
//        return jdbcUserDetailsManager;
//    }

    @RefreshScope
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
    public HandlerInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @RefreshScope
    @Bean
    @Primary
    public TokenStore tokenStore() {
        if (CloudPlatform.HEROKU.isActive(this.env) || Arrays.asList(this.env.getActiveProfiles()).contains("poweredge")) {
            return new JwtTokenStore(this.jwtTokenConverter);
        } else {
            return new JdbcTokenStore(dataSource);
        }
    }

    @RefreshScope
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setTokenStore(tokenStore());
        if (CloudPlatform.HEROKU.isActive(this.env) || Arrays.asList(this.env.getActiveProfiles()).contains("poweredge")) {
            defaultTokenServices.setTokenEnhancer(this.jwtTokenConverter);
        } else {
            defaultTokenServices.setTokenEnhancer(this.tokenEnhancer);
        }
        return defaultTokenServices;
    }

    @RefreshScope
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }

    @RefreshScope
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

    @RefreshScope
    @Bean
    @Primary
    public JdbcOperations jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @RefreshScope
    @Bean
    public TransactionOperations transactionOperations(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @RefreshScope
    @Bean
    @ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "cloudinary")
    public Cloudinary cloudinary() {
        return new Cloudinary(cloudinaryUrl);
    }

    @RefreshScope
    @Bean
    @ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "firebase")
    public StorageClient firebaseStorage() {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(this.firebaseCredentials.getBytes()));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl(this.firebaseDatabaseUrl)
                    .setStorageBucket(this.firebaseStorageBucket)
                    .build();

            FirebaseApp fireApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            if (firebaseApps != null && !firebaseApps.isEmpty()) {
                for (FirebaseApp app : firebaseApps) {
                    if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                        fireApp = app;
                }
            } else
                fireApp = FirebaseApp.initializeApp(options);
            return StorageClient.getInstance(Objects.requireNonNull(fireApp));
        } catch (IOException ex) {
            throw new FileStorageException("Could not get admin-sdk json file. Please try again!", ex);
        }
    }

    @RefreshScope
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sessionManagerCustomizer() {
        return server -> {
            ErrorPage error400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400");
            ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
            ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
            ErrorPage error403 = new ErrorPage(HttpStatus.FORBIDDEN, "/error/403");
            ErrorPage error503 = new ErrorPage(HttpStatus.SERVICE_UNAVAILABLE, "/error/503");
            ErrorPage error401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401");
            server.addErrorPages(error400, error500, error404, error403, error503
                    , error401
            );

        };
    }

    @RefreshScope
    @Bean
    @Primary
    @ConditionalOnCloudPlatform(CloudPlatform.HEROKU)
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @RefreshScope
    @Bean
    @Primary
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");
        serializer.setCookiePath("/");
        switch (activeProfile) {
            case "heroku":
                serializer.setDomainName("lambda-auth-service.herokuapp.com");
                break;
            default:
                serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        }
        return serializer;
    }

    @RefreshScope
    @Bean
    @ConditionalOnCloudPlatform(CloudPlatform.NONE)
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                // set to CONFIDENTIAL to automatically redirect from http to https port
                securityConstraint.setUserConstraint(securityPolicy);
//                securityConstraint.setUserConstraint("NONE");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(getHttpConnector());
        return tomcat;
    }

    private Connector getHttpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme(connectorScheme);
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        connector.addUpgradeProtocol(new Http2Protocol());
        return connector;
    }

}
