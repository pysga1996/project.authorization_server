package com.lambda.dao;

import com.lambda.model.dto.UserProfileDTO;

public interface UserProfileDao {

    UserProfileDTO findProfileById(Long id);

    void createProfile(UserProfileDTO userProfileDTO);

    void updateProfile(UserProfileDTO userProfileDTO);

}
