package com.lambda.dao.impl;

import com.lambda.constant.JdbcConstant;
import com.lambda.dao.UserDao;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Optional;

@Component
@Transactional
public class UserDaoImpl extends JdbcUserDetailsManager implements UserDao {

    private final JdbcOperations jdbcOperations;

    private final SqlResultExtractor<UserDTO> extractor;

    @Autowired
    public UserDaoImpl(DataSource dataSource, JdbcOperations jdbcOperations,
                       SqlResultExtractor<UserDTO> extractor) {
        this.extractor = extractor;
        this.setDataSource(dataSource);
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return this.jdbcOperations.query(
                JdbcConstant.DEF_USERS_BY_USERNAME_FULL_WITH_SETTING_QUERY,
                extractor.singleExtractor(), username);
    }

    @Override
    public Page<UserDTO> findByUsernameContaining(String username, Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserDTO> findByAuthorities_Authority(String authority, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return this.jdbcOperations.query(
                JdbcConstant.DEF_USERS_BY_ID_FULL_WITH_SETTING_QUERY,
                extractor.singleExtractor(), id);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return null;
    }

    @Override
    public void saveAndFlush(UserDTO user) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void save(UserDTO user) {

    }
}
