package com.lambda.config.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final Formatter<Set<String>> stringSetFormatter;

    private final Formatter<List<GrantedAuthority>> authoritiesFormatter;

    private final HandlerInterceptor localeChangeInterceptor;

    @Autowired
    public WebMvcConfig(Formatter<Set<String>> stringSetFormatter,
                        Formatter<List<GrantedAuthority>> authoritiesFormatter,
                        HandlerInterceptor localeChangeInterceptor) {
        this.stringSetFormatter = stringSetFormatter;
        this.authoritiesFormatter = authoritiesFormatter;
        this.localeChangeInterceptor = localeChangeInterceptor;
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
