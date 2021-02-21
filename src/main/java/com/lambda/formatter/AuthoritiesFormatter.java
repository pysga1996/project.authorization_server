package com.lambda.formatter;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AuthoritiesFormatter implements Formatter<List<GrantedAuthority>> {

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
}
