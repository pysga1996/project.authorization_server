package com.lambda.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * @author thanhvt
 * @created 25/04/2021 - 6:53 CH
 * @project vengeance
 * @since 1.0
 **/
@Configuration
@SuppressWarnings("deprecation")
public class JwtConfig {

    @Bean
    public KeyPair jwtKeyPair() {
        ClassPathResource ksFile =
            new ClassPathResource("vengeance.jks");
        KeyStoreKeyFactory ksFactory =
            new KeyStoreKeyFactory(ksFile, "1381996".toCharArray());
        return ksFactory.getKeyPair("zeronos", "259138".toCharArray());
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) jwtKeyPair().getPublic())
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256)
            .keyID("zeronos-key-id");
        return new JWKSet(builder.build());
    }
}
