package com.lambda.service.impl;

import com.lambda.dao.UserDao;
import com.lambda.dto.soap.user.UserDTO;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDTO getCurrentUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.getUserByUsername(username).toDTO();
    }
}
