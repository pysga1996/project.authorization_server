package com.lambda.config.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.expression.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerInterceptor localeChangeInterceptor;

    @Autowired
    public WebMvcConfig(HandlerInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
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
        registry.addFormatter(new Formatter<Set<String>>() {
            @Override
            @NonNull
            public String print(@NonNull Set<String> object, @NonNull Locale locale) {
                if (object.isEmpty()) return "";
                return new Strings(locale).setJoin(object, ",");
            }

            @Override
            @NonNull
            public Set<String> parse(String text, @NonNull Locale locale) {
                if ("".equals(text.trim())) return Collections.emptySet();
                return new Strings(locale).setSplit(text, ",");
            }
        });
        registry.addFormatter(new Formatter<List<GrantedAuthority>>() {
            @Override
            @NonNull
            public String print(@NonNull List<GrantedAuthority> object, @NonNull Locale locale) {
                if (object.isEmpty()) return "";
                return new Strings(locale).listJoin(object, ",");
            }

            @Override
            @NonNull
            public List<GrantedAuthority> parse(@NonNull String text, @NonNull Locale locale) {
                if ("".equals(text.trim())) return Collections.emptyList();

                return new Strings(locale).listSplit(text, ",")
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        });
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
