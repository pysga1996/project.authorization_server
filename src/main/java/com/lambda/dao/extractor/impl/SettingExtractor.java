package com.lambda.dao.extractor.impl;

import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.SettingDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class SettingExtractor implements SqlResultExtractor<SettingDTO> {

    @Override
    public ResultSetExtractor<Optional<SettingDTO>> singleExtractor() {
        return rs -> {
            SettingDTO setting = null;
            while (rs.next()) {
                setting = new SettingDTO();
                setting.setUsername(rs.getString("username"));
                setting.setAlert(rs.getBoolean("alert"));
                setting.setTheme(rs.getString("theme"));
            }
            return Optional.ofNullable(setting);
        };
    }

    @Override
    public ResultSetExtractor<List<SettingDTO>> customListExtractor() {
        return rs -> {
            List<SettingDTO> settingList = new ArrayList<>();
            while (rs.next()) {
                SettingDTO setting = new SettingDTO();
                setting.setUsername(rs.getString("username"));
                setting.setAlert(rs.getBoolean("alert"));
                setting.setTheme(rs.getString("theme"));
                settingList.add(setting);
            }
            return settingList;
        };
    }
}
