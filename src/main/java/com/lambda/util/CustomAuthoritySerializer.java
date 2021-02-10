package com.lambda.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomAuthoritySerializer extends JsonSerializer<Set<GrantedAuthority>> {

    @Override
    public void serialize(Set<GrantedAuthority> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Set<String> authorities = value.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        gen.writeArray((String[]) authorities.toArray(), 0, authorities.size());
    }
}
