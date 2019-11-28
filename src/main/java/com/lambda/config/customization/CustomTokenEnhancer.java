package com.lambda.config.customization;

import com.lambda.dao.UserDao;
import com.lambda.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@SuppressWarnings("deprecation")
public class CustomTokenEnhancer implements TokenEnhancer {
    private UserDao userDao;

    @Autowired
    public void setCustomTokenEnhancer(UserDao userDAOimpl) {
        this.userDao = userDAOimpl;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = userDao.getUserByUsername(authentication.getName());
        final Map<String, Object> additionalInfo = new TreeMap<>();
        additionalInfo.put("id", user.getId());
        additionalInfo.put("username", user.getUsername());
        additionalInfo.put("roles", user.getAuthorities());
        additionalInfo.put("avatarUrl", user.getAvatarUrl());
        additionalInfo.put("setting", user.getSetting());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}
