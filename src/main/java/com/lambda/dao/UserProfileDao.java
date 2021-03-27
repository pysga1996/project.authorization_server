package com.lambda.dao;

import com.lambda.model.dto.UserProfileDTO;

import java.util.Optional;

public interface UserProfileDao {

    Optional<UserProfileDTO> findProfileByUsername(String username);

    void createProfile(UserProfileDTO userProfileDTO);

    void updateProfile(UserProfileDTO userProfileDTO);

}
