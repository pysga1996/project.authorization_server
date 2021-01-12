package com.lambda.dao.impl;

import com.lambda.dao.ClientDao;
import com.lambda.mapper.ClientMapper;

import com.lambda.model.Client;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class ClientDaoImpl extends JdbcDaoSupport implements ClientDao {

    private Object[] params = new Object[] {};
    private final ClientMapper mapper = new ClientMapper();

    public ClientDaoImpl(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Override
    public List<Client> getAllClients() {
        String query = "SELECT * FROM oauth_client_details";
        return Objects.requireNonNull(this.getJdbcTemplate()).query(query, params, mapper);
    }

    @Override
    public Client getClientById(String id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        String query = "SELECT * FROM oauth_client_details WHERE client_id=:id";
        return Objects.requireNonNull(this.getJdbcTemplate()).queryForObject(query, mapper, namedParameters);
    }

    @Override
    public void saveOrUpdateClient(Client client) {
        params = new Object[] {client.getId(), client.getResourceIds(), client.getClientSecret(), client.getScope(),
                client.getAuthorizedGrantTypes(), client.getWebServerRedirectUri(), client.getAuthorities(), client.getAccessTokenValidity(),
                client.getRefreshTokenValidity(), client.getAdditionalInformation(), client.getAutoApprove()};
        String query = "INSERT INTO oauth_client_details VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Objects.requireNonNull(this.getJdbcTemplate()).update(query, params);
    }

    @Override
    public void deleteClientById(String id) {
        String query = "DELETE FROM oauth_client_details WHERE oauth_client_details.client_id =?";
        Objects.requireNonNull(this.getJdbcTemplate()).update(query, id);
    }
}
