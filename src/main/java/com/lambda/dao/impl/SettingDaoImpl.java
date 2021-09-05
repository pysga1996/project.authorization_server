package com.lambda.dao.impl;

import com.lambda.dao.SettingDao;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.SettingDTO;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class SettingDaoImpl implements SettingDao {

    private final JdbcOperations jdbcOperations;

    private final SqlResultExtractor<SettingDTO> settingExtractor;

    @Autowired
    public SettingDaoImpl(JdbcOperations jdbcOperations,
        SqlResultExtractor<SettingDTO> settingExtractor) {
        this.jdbcOperations = jdbcOperations;
        this.settingExtractor = settingExtractor;
    }

    @Override
    public Optional<SettingDTO> getSetting(String username) {
        String sql = "SELECT username, alert, theme FROM setting WHERE username = ?";
        return this.jdbcOperations.query(sql, this.settingExtractor.singleExtractor(), username);
    }

    @Override
    public void create(SettingDTO setting) {
        String username = setting.getUsername();
        Boolean alert = setting.getAlert();
        String theme = setting.getTheme();
        String sql = "INSERT INTO setting(username, alert, theme) VALUES(?, ?, ?)";
        this.jdbcOperations.update(sql, username, alert, theme);
    }

    @Override
    public void update(SettingDTO setting) {
        String username = setting.getUsername();
        Boolean alert = setting.getAlert();
        String theme = setting.getTheme();
        String sql = "UPDATE setting SET alert = ?, theme = ? WHERE username = ?";
        this.jdbcOperations.update(sql, alert, theme, username);
    }
}
