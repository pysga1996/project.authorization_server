package com.lambda.dao.extractor.impl;

import com.lambda.model.dto.ClientDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientExtractor implements RowMapper<ClientDTO> {
    @Override
    public ClientDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
