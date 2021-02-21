package com.lambda.formatter;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Strings;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

@Component
public class StringSetFormatter implements Formatter<Set<String>> {

    @Override
    @NonNull
    public String print(@NonNull Set<String> object, @NonNull Locale locale) {
        if (object.isEmpty()) return "";
        return new Strings(locale).setJoin(object, ",");
    }

    @Override
    @NonNull
    public Set<String> parse(@NonNull String text, @NonNull Locale locale) {
        if ("".equals(text.trim())) return Collections.emptySet();
        return new Strings(locale).setSplit(text, ",");
    }
}
