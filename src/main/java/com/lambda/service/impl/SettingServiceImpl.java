package com.lambda.service.impl;

import com.lambda.dao.SettingDao;
import com.lambda.model.dto.SettingDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingDao settingDAO;

    @Autowired
    public SettingServiceImpl(SettingDao settingDAO) {
        this.settingDAO = settingDAO;
    }

    @Override
    public void createSetting(UserDTO user) {
        SettingDTO settingDTO = new SettingDTO();
//        settingDTO.setUser(user);
        this.settingDAO.save(settingDTO);
    }

    @Override
    public void changeSetting(SettingDTO settingDTO) {
//        settingDTO.setUser(this.getCurrentUser());
        this.settingDAO.save(settingDTO);
    }
}
