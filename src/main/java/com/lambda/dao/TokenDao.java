package com.lambda.dao;


import com.lambda.constant.TokenStatus;
import com.lambda.constant.TokenType;
import com.lambda.model.dto.AuthenticationTokenDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDao {

    AuthenticationTokenDTO findToken(String token, TokenType registration, TokenStatus status);

    void insertToken(String token, String username, TokenType registration, TokenStatus status, String redirectUrl);

    void updateToken(Long id, TokenStatus status);
}
