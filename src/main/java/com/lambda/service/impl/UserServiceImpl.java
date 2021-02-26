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
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final TokenDao tokenDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, TokenDao tokenDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getUserList(Pageable pageable) {
        return this.userDao.userList(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserDTO> optionalUserDTO = userDao.findByUsername(username);
        return optionalUserDTO.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional
    public void register(UserDTO user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userDao.register(user);
    }

    @Override
    @Transactional
    public void unregister(String username) {
        this.userDao.unRegister(username);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional
    public void updateUser(String username, String password, boolean enabled, boolean accountLocked, boolean accountExpired,
                           boolean credentialsExpired, String groups) {

        String newPassword = null;
        if (password !=null && !password.trim().isEmpty()) {
            newPassword  = this.passwordEncoder.encode(password);
        }
        String[] insertArr = Strings.split(groups, ',');
        Set<Long> insertSet = new HashSet<>(Arrays.asList(insertArr)).stream()
                .filter(e -> (e != null && Pattern.compile("^\\d+$").matcher(e).matches()))
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        Set<Long> tmpSet = new HashSet<>(insertSet);
        Set<Long> existSet = this.userDao.findGroupIdSetByUsername(username);
        tmpSet.retainAll(existSet);
        insertSet.removeAll(tmpSet);
        existSet.removeAll(tmpSet);
        this.userDao.updateUserAndGroup(username, newPassword, enabled, accountLocked,
                accountExpired, credentialsExpired, insertSet, existSet);
    }
}
