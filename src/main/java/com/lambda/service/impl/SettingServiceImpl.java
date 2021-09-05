package com.lambda.service.impl;

import com.lambda.dao.SettingDao;
import com.lambda.model.dto.SettingDTO;
import com.lambda.service.SettingService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingDao settingDAO;

    @Autowired
    public SettingServiceImpl(SettingDao settingDAO) {
        this.settingDAO = settingDAO;
    }

    @Override
    @Transactional
    public SettingDTO getSetting() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<SettingDTO> existedSetting = this.settingDAO.getSetting(username);
        if (existedSetting.isPresent()) {
            return existedSetting.get();
        } else {
            SettingDTO setting = new SettingDTO();
            setting.setUsername(username);
            setting.setAlert(false);
            setting.setTheme("light");
            this.settingDAO.create(setting);
            return setting;
        }
    }

    @Override
    @Transactional
    public void changeSetting(SettingDTO setting) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<SettingDTO> existedSetting = this.settingDAO.getSetting(username);
        setting.setUsername(username);
        if (existedSetting.isPresent()) {
            this.settingDAO.update(setting);
        } else {
            this.settingDAO.create(setting);
        }
    }
}
