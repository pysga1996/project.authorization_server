package com.lambda.service;

import com.lambda.model.dto.UserProfileDTO;

public interface UserProfileService {

    UserProfileDTO getProfileById(Long id);

    UserProfileDTO getCurrentProfile();

    void createProfile(UserProfileDTO userProfileDTO);

    void updateProfile(UserProfileDTO userProfileDTO);
}
