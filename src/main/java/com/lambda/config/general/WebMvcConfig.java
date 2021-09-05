package com.lambda.config.general;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@RefreshScope
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final Formatter<Set<String>> stringSetFormatter;

    private final Formatter<List<GrantedAuthority>> authoritiesFormatter;

    private final Formatter<Timestamp> timestampFormatter;

    private final HandlerInterceptor localeChangeInterceptor;

    private final MessageSource messageSource;

    @Autowired
    public WebMvcConfig(Formatter<Set<String>> stringSetFormatter,
        Formatter<List<GrantedAuthority>> authoritiesFormatter,
        Formatter<Timestamp> timestampFormatter, HandlerInterceptor localeChangeInterceptor,
        MessageSource messageSource) {
        this.stringSetFormatter = stringSetFormatter;
        this.authoritiesFormatter = authoritiesFormatter;
        this.timestampFormatter = timestampFormatter;
        this.localeChangeInterceptor = localeChangeInterceptor;
        this.messageSource = messageSource;
    }

    @Bean
    @Primary
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("language");
        localeResolver.setCookieHttpOnly(true);
        localeResolver.setCookiePath("/");
        localeResolver.setCookieSecure(false);
        localeResolver.setRejectInvalidCookies(false);
        return localeResolver;
    }

    @Bean
    @Primary
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

//    @Bean
//    @Primary
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
////        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
//        configuration.setAllowedOrigins(CrossOriginConfig.allowedOrigins);
//        configuration.setAllowedMethods(Collections.singletonList("*"));
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/homepage.html");
        registry.addViewController("/login.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/js/**", "/image/**", "/icon/**")
            .addResourceLocations("classpath:/static/css/",
                "classpath:/static/js/",
                "classpath:/static/image/",
                "classpath:/static/icon/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(stringSetFormatter);
        registry.addFormatter(authoritiesFormatter);
        registry.addFormatter(timestampFormatter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }
}
