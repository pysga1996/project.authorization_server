package com.lambda.config.custom;

import com.lambda.dao.UserDao;
import com.lambda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    private final UserDao userDao;

    @Autowired
    public CustomTokenEnhancer(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String username = authentication.getUserAuthentication().getName();
//        User user = userDao.getUserByUsername(username);
        final Map<String, Object> additionalInfo = new TreeMap<>();
//        additionalInfo.put("id", user.getId());
        additionalInfo.put("user_name", username);
//        additionalInfo.put("roles", user.getAuthorities());
//        additionalInfo.put("avatar_url", user.getAvatarUrl());
//        additionalInfo.put("setting", user.getSetting());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}
