package com.lambda.service;

import com.lambda.model.dto.UserProfileDTO;

public interface UserProfileService {

    UserProfileDTO getProfileByUsername(String username);

    void createProfile(UserProfileDTO userProfileDTO);

    void updateProfile(UserProfileDTO userProfileDTO);
}
