package com.lambda.dao.impl;

import com.lambda.dao.ClientDao;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
@Transactional
@SuppressWarnings("deprecation")
public class ClientDaoImpl extends JdbcClientDetailsService implements ClientDao {

    public ClientDaoImpl(DataSource dataSource) {
        super(dataSource);
    }
}
