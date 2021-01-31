package com.lambda.service.impl;

import com.lambda.dao.UserProfileDao;
import com.lambda.error.BusinessException;
import com.lambda.error.UserNotFoundException;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserProfileService;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserService userService;

    private final UserProfileDao userProfileDao;

    @Autowired
    public UserProfileServiceImpl(UserProfileDao userProfileDao, UserService userService) {
        this.userProfileDao = userProfileDao;
        this.userService = userService;
    }

    @Override
    public UserProfileDTO getProfileById(Long id) {
        return this.userProfileDao.findProfileById(id);
    }

    @Override
    public UserProfileDTO getCurrentProfile() {
        UserDTO userDTO = this.userService.getCurrentUser();
        if (userDTO != null) {
            return userDTO.getUserProfile();
        } throw new UserNotFoundException();
    }

    @Override
    @Transactional
    public void createProfile(UserProfileDTO userProfileDTO) {
        UserDTO userDTO = this.userService.getCurrentUser();
        if (userDTO != null) {
            if (userDTO.getUserProfile() != null) {
                throw new BusinessException(1250, "User profile existed!");
            }
            userProfileDTO.setUserId(userDTO.getId());
            this.userProfileDao.createProfile(userProfileDTO);
        } else throw new UserNotFoundException();
    }

    @Override
    @Transactional
    public void updateProfile(UserProfileDTO userProfileDTO) {
        UserDTO userDTO = this.userService.getCurrentUser();
        if (userDTO != null) {
            if (userDTO.getUserProfile() == null) {
                throw new BusinessException(1250, "User profile not found!");
            }
            userProfileDTO.setId(userDTO.getUserProfile().getId());
            this.userProfileDao.updateProfile(userProfileDTO);
        } else throw new UserNotFoundException();
    }
}
