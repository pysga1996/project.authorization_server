package com.lambda.mapper;

import com.lambda.model.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {

        String id = rs.getString("client_id");

        String resourceIds = rs.getString("resource_ids");

        String clientSecret = rs.getString("client_secret");

        String scope = rs.getString("scope");

        String authorizedGrantTypes = rs.getString("authorized_grant_types");

        String webServerRedirectUri = rs.getString("web_server_redirect_uri");

        String authorities = rs.getString("authorities");

        Integer accessTokenValidity = rs.getInt("access_token_validity");

        Integer refreshTokenValidity = rs.getInt("refresh_token_validity");

        String additionalInformation = rs.getString("additional_information");

        String autoApprove = rs.getString("autoapprove");

        return new Client(id, resourceIds, clientSecret, scope, authorizedGrantTypes, webServerRedirectUri,
                authorities, accessTokenValidity, refreshTokenValidity, additionalInformation, autoApprove);
    }
}
