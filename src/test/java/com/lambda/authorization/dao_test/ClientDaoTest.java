package com.lambda.authorization.dao_test;

import com.lambda.authorization.AuthorizationTestConfiguration;
import com.lambda.mapper.ClientMapper;
import com.lambda.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AuthorizationTestConfiguration.class)
public class ClientDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RowMapper<Client> clientMapper = new ClientMapper();

    @Test
    public void whenFindAll_thenReturn3Records() {
        List<Client> clientList = jdbcTemplate.query("SELECT * FROM oauth_client_details", clientMapper);
        assertThat(clientList).hasSize(3);
    }

    @Test
    public void whenFindById_thenReturn1Record() {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", "fooClientIdPassword");
        Client fooClient = namedParameterJdbcTemplate.queryForObject("SELECT * FROM oauth_client_details WHERE client_id=:id", namedParameters, clientMapper);
        assertThat(fooClient).isNotNull();
        assertThat(passwordEncoder.matches("secret", fooClient.getClientSecret())).isTrue();
    }

    @Test
    public void whenSave_thenFoundInDb() {
        int numberOfRowsAffected = jdbcTemplate.update("INSERT INTO oauth_client_details " +
                "VALUE ('climaxSoundClient', null, '123456', 'listen, upload', 'password,authorization_code,refresh_token,client_credentials'," +
                " null, null, 36000, 36000, null, '1')");
        assertThat(numberOfRowsAffected).isEqualTo(1);
    }

    @Test
    public void whenDeleteById_thenNotFoundInDb() {
        int numberOfRowsAffected = jdbcTemplate.update("DELETE FROM oauth_client_details WHERE oauth_client_details.client_id=?", "barClientIdPassword");
        assertThat(numberOfRowsAffected).isEqualTo(1);
    }
}
