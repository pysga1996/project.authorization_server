package com.lambda.service;

import com.lambda.model.dto.SettingDTO;

public interface SettingService {

    SettingDTO getSetting();

    void changeSetting(SettingDTO settingDTO);
}
