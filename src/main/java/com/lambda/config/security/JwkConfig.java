package com.lambda.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * @author thanhvt
 * @created 25/04/2021 - 6:53 CH
 * @project vengeance
 * @since 1.0
 **/
@Configuration
@SuppressWarnings("deprecation")
public class JwkConfig {

    @Value("${custom.jwk.keystore}")
    private Resource jwkKeyStore;

    @Value("${custom.jwk.keystore-password}")
    private String jwkKeyStorePassword;

    @Value("${custom.jwk.key}")
    private String jwkKey;

    @Value("${custom.jwk.key-pass}")
    private String jwkKeyPass;

    @Value("${custom.jwk.key-id}")
    private String jwkKeyId;

    @Bean
    public KeyPair jwtKeyPair() {
        KeyStoreKeyFactory ksFactory =
            new KeyStoreKeyFactory(jwkKeyStore, jwkKeyStorePassword.toCharArray());
        return ksFactory.getKeyPair(jwkKey, jwkKeyPass.toCharArray());
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) jwtKeyPair().getPublic())
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256)
            .keyID(jwkKeyId);
        return new JWKSet(builder.build());
    }
}
