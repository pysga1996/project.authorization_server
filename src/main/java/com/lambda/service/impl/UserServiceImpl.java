package com.lambda.service.impl;

import com.lambda.constant.TokenStatus;
import com.lambda.constant.TokenType;
import com.lambda.dao.TokenDao;
import com.lambda.dao.UserDao;
import com.lambda.error.BusinessException;
import com.lambda.model.dto.AuthenticationTokenDTO;
import com.lambda.model.dto.SearchResponseDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_GROUP = "user";

    private final UserDao userDao;

    private final TokenDao tokenDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, TokenDao tokenDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
    }

    @Override
    public UserDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserDTO> optionalUserDTO = userDao.findByUsername(username);
        return optionalUserDTO.orElse(null);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void save(UserDTO user, boolean createAction) {
        if (createAction) {
            if (userDao.findByUsername(user.getUsername()).isPresent()) {
                throw new BusinessException(1000, "Username existed");
            }
            user.setEnabled(false);
            userDao.createUser(user);
            userDao.addUserToGroup(user.getUsername(), DEFAULT_GROUP);
        } else {
            userDao.updateUser(user);
        }
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public SearchResponseDTO search(String searchText) {
        return new SearchResponseDTO();
    }

    @Override
    @Transactional
    public String confirmRegistration(String token) {
        AuthenticationTokenDTO authenticationTokenDTO =
                this.tokenDao.findToken(token, TokenType.REGISTRATION, TokenStatus.ACTIVE);
        this.validateToken(authenticationTokenDTO);
        String url = authenticationTokenDTO.getRedirectUrl();
        this.tokenDao.updateToken(authenticationTokenDTO.getId(), TokenStatus.USED);
        return url;
    }

    @Override
    @Transactional
    public String showChangePasswordPage(String token) {
        AuthenticationTokenDTO authenticationTokenDTO =
                this.tokenDao.findToken(token, TokenType.RESET_PASSWORD, TokenStatus.INACTIVE);
        this.validateToken(authenticationTokenDTO);
        String url = authenticationTokenDTO.getRedirectUrl();
        this.tokenDao.updateToken(authenticationTokenDTO.getId(), TokenStatus.ACTIVE);
        return url;
    }


    private boolean emailExist(String email) {
        UserDTO user = userDao.findByEmail(email);
        return user != null;
    }

    @Override
    public UserDTO checkResetPassToken(String token) {
        AuthenticationTokenDTO authenticationTokenDTO = this.tokenDao.findToken(token, TokenType.RESET_PASSWORD, TokenStatus.ACTIVE);
        this.validateToken(authenticationTokenDTO);
        this.tokenDao.updateToken(authenticationTokenDTO.getId(), TokenStatus.USED);
        Optional<UserDTO> optionalUserDTO =
                this.userDao.findByUsername(authenticationTokenDTO.getUsername());
        return optionalUserDTO.orElseThrow(() -> new BusinessException(1001, "User was not found or might be removed from our system"));
    }
}
