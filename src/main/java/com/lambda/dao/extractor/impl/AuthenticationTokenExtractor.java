package com.lambda.dao.extractor.impl;

import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.AuthenticationTokenDTO;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationTokenExtractor implements SqlResultExtractor<AuthenticationTokenDTO> {

    @Override
    public ResultSetExtractor<Optional<AuthenticationTokenDTO>> singleExtractor() {
        return null;
    }

    @Override
    public RowMapper<AuthenticationTokenDTO> listExtractor() {
        return null;
    }
}
