package com.lambda.dao_test;

import static org.assertj.core.api.Assertions.assertThat;

import com.lambda.AuthorizationTestConfiguration;
import com.lambda.dao.extractor.impl.ClientExtractor;
import com.lambda.model.dto.ClientDTO;
import java.util.List;
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

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AuthorizationTestConfiguration.class)
public class ClientDTODaoTest {

    private final RowMapper<ClientDTO> clientMapper = new ClientExtractor();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void whenFindAll_thenReturn3Records() {
        List<ClientDTO> clientDTOList = jdbcTemplate
            .query("SELECT * FROM oauth_client_details", clientMapper);
        assertThat(clientDTOList).hasSize(3);
    }

    @Test
    public void whenFindById_thenReturn1Record() {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", "fooClientIdPassword");
        ClientDTO fooClientDTO = namedParameterJdbcTemplate
            .queryForObject("SELECT * FROM oauth_client_details WHERE client_id=:id",
                namedParameters, clientMapper);
        assertThat(fooClientDTO).isNotNull();
        assertThat(passwordEncoder.matches("secret", fooClientDTO.getClientSecret())).isTrue();
    }

    @Test
    public void whenSave_thenFoundInDb() {
        int numberOfRowsAffected = jdbcTemplate.update("INSERT INTO oauth_client_details " +
            "VALUE ('climaxSoundClient', null, '123456', 'listen, upload', 'password,authorization_code,refresh_token,client_credentials',"
            +
            " null, null, 36000, 36000, null, '1')");
        assertThat(numberOfRowsAffected).isEqualTo(1);
    }

    @Test
    public void whenDeleteById_thenNotFoundInDb() {
        int numberOfRowsAffected = jdbcTemplate
            .update("DELETE FROM oauth_client_details WHERE oauth_client_details.client_id=?",
                "barClientIdPassword");
        assertThat(numberOfRowsAffected).isEqualTo(1);
    }
}
