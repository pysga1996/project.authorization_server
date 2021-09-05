package com.lambda.service.impl;

import com.lambda.dao.UserProfileDao;
import com.lambda.error.BusinessException;
import com.lambda.error.UserNotFoundException;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserProfileService;
import com.lambda.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Transactional
    public UserProfileDTO getProfileByUsername(String username) {
        if (username == null) {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        Optional<UserProfileDTO> optionalUserProfile = this.userProfileDao
            .findProfileByUsername(username);
        ;
        if (!optionalUserProfile.isPresent()) {
            UserProfileDTO userProfile = new UserProfileDTO();
            userProfile.setUsername(username);
            this.userProfileDao.createProfile(userProfile);
            return userProfile;
        }
        return optionalUserProfile.get();
    }

    @Override
    @Transactional
    public void createProfile(UserProfileDTO userProfileDTO) {
        UserDTO userDTO = this.userService.getCurrentUser();
        if (userDTO != null) {
            if (userDTO.getUserProfile() != null) {
                throw new BusinessException(1250, "User profile existed!");
            }
            userProfileDTO.setUsername(userDTO.getUsername());
            this.userProfileDao.createProfile(userProfileDTO);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    public void updateProfile(UserProfileDTO userProfileDTO) {
        UserDTO userDTO = this.userService.getCurrentUser();
        if (userDTO != null) {
            if (userDTO.getUserProfile() == null) {
                throw new BusinessException(1250, "User profile not found!");
            }
            userProfileDTO.setUsername(userDTO.getUserProfile().getUsername());
            this.userProfileDao.updateProfile(userProfileDTO);
        } else {
            throw new UserNotFoundException();
        }
    }
}
