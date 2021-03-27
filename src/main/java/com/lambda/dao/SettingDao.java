package com.lambda.dao;

import com.lambda.model.dto.SettingDTO;

import java.util.Optional;

public interface SettingDao {

    Optional<SettingDTO> getSetting(String username);

    void create(SettingDTO setting);

    void update(SettingDTO setting);
}
