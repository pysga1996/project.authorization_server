package com.lambda.config.custom;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Collections;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

/**
 * @author thanhvt
 * @created 25/04/2021 - 5:49 CH
 * @project vengeance
 * @since 1.0
 **/
@Log4j2
@Component("customJwtTokenConverter")
@SuppressWarnings("deprecation")
public class CustomJwtTokenConverter extends JwtAccessTokenConverter implements TokenEnhancer {

    private final TokenEnhancer tokenEnhancer;

    private final Map<String, String> customHeaders;

    private final JsonParser objectMapper = JsonParserFactory.create();
    final RsaSigner signer;

    @Autowired
    public CustomJwtTokenConverter(
        @Qualifier("customTokenEnhancer") TokenEnhancer tokenEnhancer, KeyPair keyPair) {
        super();
        super.setKeyPair(keyPair);
        this.tokenEnhancer = tokenEnhancer;
        this.signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
        this.customHeaders = Collections.singletonMap("kid", "zeronos-key-id");
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
        OAuth2Authentication authentication) {
        this.tokenEnhancer.enhance(accessToken, authentication);
        return super.enhance(accessToken, authentication);
    }

    @Override
    protected String encode(OAuth2AccessToken accessToken,
        OAuth2Authentication authentication) {
        String content;
        try {
            content = this.objectMapper
                .formatMap(getAccessTokenConverter()
                    .convertAccessToken(accessToken, authentication));
        } catch (Exception ex) {
            log.error(ex);
            throw new IllegalStateException(
                "Cannot convert access token to JSON", ex);
        }
        return JwtHelper.encode(
            content,
            this.signer,
            this.customHeaders).getEncoded();
    }
}
