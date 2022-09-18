package com.lambda.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author pysga
 * @created 7/19/2022 - 9:48 PM
 * @project alpha-sound-service
 * @since 1.0
 **/
@SuppressWarnings("deprecation")
public class CustomTokenServices extends DefaultTokenServices {

    @Override
    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken oAuth2AccessToken = super.createAccessToken(authentication);
        Optional<HttpServletResponse> responseOptional = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ra -> ra instanceof ServletRequestAttributes)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getResponse);
        responseOptional.ifPresent(httpServletResponse -> httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, "Bearer " + oAuth2AccessToken.getValue()));
        return oAuth2AccessToken;
    }
}
