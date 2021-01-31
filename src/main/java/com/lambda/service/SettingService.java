package com.lambda.service;

import com.lambda.model.dto.SettingDTO;
import com.lambda.model.dto.UserDTO;

public interface SettingService {

    void createSetting(UserDTO user);

    void changeSetting(SettingDTO settingDTO);
}
