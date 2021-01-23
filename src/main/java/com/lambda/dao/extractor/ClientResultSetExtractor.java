package com.lambda.dao.extractor;

import com.lambda.model.CustomClient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClientResultSetExtractor implements RowMapper<CustomClient> {

    @Override
    public CustomClient mapRow(ResultSet rs, int rowNum) throws SQLException {

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
        return null;
//        return new CustomClient(id, resourceIds, clientSecret, scope, authorizedGrantTypes, webServerRedirectUri,
//                authorities, accessTokenValidity, refreshTokenValidity, additionalInformation, autoApprove);
    }
}
