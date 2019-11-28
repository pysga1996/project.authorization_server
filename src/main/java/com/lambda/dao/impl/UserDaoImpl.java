package com.lambda.dao.impl;

import com.lambda.dao.UserDao;
import com.lambda.mapper.UserResultSetExtractor;
import com.lambda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private UserResultSetExtractor mapper = new UserResultSetExtractor();

    @Autowired
    public UserDaoImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.setDataSource(dataSource);
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Client> getAllClients() {
//        String query = "SELECT * FROM oauth_client_details";
//        return Objects.requireNonNull(this.getJdbcTemplate()).query(query, params, mapper);
//    }

    @Override
    public User getUserByUsername(String username) {
        String query = "SELECT lambda_user.id, password, username, role.id AS role_id, role.authority AS role_authority, "
                + " setting.id AS setting_id, setting.dark_mode AS setting_dark_mode, "
                + " account_non_expired, account_non_locked, credentials_non_expired, enabled, avatar_url FROM"
                + " lambda_user INNER JOIN user_role ON lambda_user.id = user_role.user_id "
                + " INNER JOIN role ON user_role.role_id = role.id "
                + " INNER JOIN setting ON lambda_user.id = setting.user_id"
                + " WHERE username=?";

        List<User> results =  jdbcTemplate.query(query, mapper, username);
        assert results != null;
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
//        return Objects.requireNonNull(this.getJdbcTemplate()).queryForObject(query, mapper, username);
    }
}
