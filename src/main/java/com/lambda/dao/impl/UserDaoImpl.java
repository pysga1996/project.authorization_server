package com.lambda.dao.impl;

import com.lambda.constant.JdbcConstant;
import com.lambda.dao.UserDao;
import com.lambda.model.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Component
@Transactional
public class UserDaoImpl extends JdbcUserDetailsManager implements UserDao {

    private final JdbcOperations jdbcOperations;

    private final ResultSetExtractor<List<CustomUser>> mapper;

    @Autowired
    public UserDaoImpl(DataSource dataSource, JdbcOperations jdbcOperations, ResultSetExtractor<List<CustomUser>> mapper) {
        this.mapper = mapper;
        this.setDataSource(dataSource);
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public CustomUser getUserByUsername(String username) {
        List<CustomUser> customUserList = jdbcOperations.query(JdbcConstant.DEF_USERS_BY_USERNAME_FULL_WITH_SETTING_QUERY, mapper, username);
        if (customUserList == null || customUserList.isEmpty()) {
            return null;
        } else {
            return customUserList.get(0);
        }
    }
}
