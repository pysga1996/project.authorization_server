package com.lambda.dao.impl;

import static com.lambda.constant.JdbcConstant.TOKEN_DURATION;

import com.lambda.constant.TokenStatus;
import com.lambda.constant.TokenType;
import com.lambda.dao.TokenDao;
import com.lambda.model.dto.AuthenticationTokenDTO;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

@Log4j2
@Repository
public class TokenDaoImpl implements TokenDao {

    private final JdbcOperations jdbcOperations;

    private final TransactionOperations transactionOperations;

    private final RowMapper<AuthenticationTokenDTO> mapper;

    @Autowired
    public TokenDaoImpl(JdbcOperations jdbcOperations,
        TransactionOperations transactionOperations) {
        this.jdbcOperations = jdbcOperations;
        this.transactionOperations = transactionOperations;
        this.mapper = BeanPropertyRowMapper.newInstance(AuthenticationTokenDTO.class);
    }

    @Override
    public AuthenticationTokenDTO findToken(String token, String email,
        TokenType registration,
        TokenStatus status) {
        String sql = "SELECT * FROM authentication_token " +
            " WHERE token = :token AND email = :email AND  type = :type AND status = :status";
        return this.jdbcOperations
            .queryForObject(sql, mapper, email, token, registration.getValue(), status.getValue());
    }

    @Override
    public void insertToken(String token, String username, TokenType type, TokenStatus status,
        String redirectUrl) {
        this.transactionOperations.executeWithoutResult((transactionStatus) -> {
            Timestamp createDate = Timestamp.from(Instant.now());
            Timestamp expireDate = Timestamp
                .from(Instant.ofEpochMilli(createDate.getTime() + TOKEN_DURATION));
            String sql = "INSERT INTO authentication_token" +
                " (token, type, username, status, create_date, expire_date, redirect_url) " +
                " VALUES(:token, :type, :username, :status, :createDate, :expireDate, :redirectUrl) ";
            this.jdbcOperations.update(sql, token, type.getValue(), status.getValue(),
                status.getValue(), createDate, expireDate, redirectUrl);
            log.info("Transaction status: {}", transactionStatus);
        });
    }

    @Override
    public void updateToken(Long id, TokenStatus status) {
        String sql = "UPDATE authentication_token SET status = :status " +
            " WHERE id = :id";
        this.jdbcOperations.update(sql, status.getValue(), id);
    }
}
