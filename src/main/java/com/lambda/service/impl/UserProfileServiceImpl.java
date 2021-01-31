package com.lambda.service.impl;

import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserProfileService;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Override
    public UserProfileDTO getProfileById(Long id) {
        return null;
    }

    @Override
    public UserProfileDTO getCurrentProfile() {
        return null;
    }

    @Override
    public void createProfile(UserProfileDTO userProfileDTO) {

    }

    @Override
    public void updateProfile(UserProfileDTO userProfileDTO) {

    }
}
