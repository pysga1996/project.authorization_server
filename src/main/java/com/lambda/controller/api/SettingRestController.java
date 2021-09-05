package com.lambda.controller.api;

import com.lambda.model.dto.SettingDTO;
import com.lambda.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setting")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true",
    exposedHeaders = {HttpHeaders.SET_COOKIE})
public class SettingRestController {

    private final SettingService settingService;

    @Autowired
    public SettingRestController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<SettingDTO> getSetting() {
        SettingDTO setting = this.settingService.getSetting();
        return new ResponseEntity<>(setting, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Void> changeSetting(@RequestBody SettingDTO settingDTO) {
        this.settingService.changeSetting(settingDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
