package com.lambda.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lambda.constant.TokenStatus;
import com.lambda.constant.TokenType;
import com.lambda.dao.TokenDao;
import com.lambda.dao.UserDao;
import com.lambda.error.BusinessException;
import com.lambda.model.domain.Group;
import com.lambda.model.dto.AuthenticationTokenDTO;
import com.lambda.model.dto.RegistrationDTO.RegistrationConfirmDTO;
import com.lambda.model.dto.ResetPasswordDTO.ChangePasswordConfirmDTO;
import com.lambda.model.dto.SearchResponseDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
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
    public Map<String, UserProfileDTO> getUserStatusMap() {
        return this.userDao.userList();
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
        Optional<UserDTO> optionalUserDTO = this.userDao.findByUsername(username);
        return optionalUserDTO.orElse(null);
    }

    @Override
    public Map<String, Object> getUserShortInfo(String username) {
        return this.userDao.getCurrentUserShortInfo(username);
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
    public void confirmRegistration(
        RegistrationConfirmDTO registrationConfirmDTO) {
        AuthenticationTokenDTO authenticationTokenDTO =
            this.tokenDao.findToken(registrationConfirmDTO.getToken(),
                registrationConfirmDTO.getEmail(),
                TokenType.REGISTRATION, TokenStatus.ACTIVE);
        this.validateToken(authenticationTokenDTO);
        UserDTO userDTO =
            this.userDao.findByUsername(authenticationTokenDTO.getUsername())
                .orElseThrow(() -> new BusinessException(1001,
                    "User was not found or might be removed from our system"));
        userDTO.setEnabled(true);
        this.userDao.updateUser(userDTO);
        this.tokenDao.updateToken(authenticationTokenDTO.getId(), TokenStatus.USED);
    }


    private boolean emailExist(String email) {
        UserDTO user = this.userDao.findByEmail(email);
        return user != null;
    }

    @Override
    @Transactional(readOnly = true)
    public void resetPassword(ChangePasswordConfirmDTO changePasswordConfirmDTO) {
        AuthenticationTokenDTO authenticationTokenDTO = this.tokenDao
            .findToken(changePasswordConfirmDTO.getToken(), changePasswordConfirmDTO.getEmail(),
                TokenType.RESET_PASSWORD, TokenStatus.ACTIVE);
        this.validateToken(authenticationTokenDTO);
        this.tokenDao.updateToken(authenticationTokenDTO.getId(), TokenStatus.USED);
        UserDTO userDTO =
            this.userDao.findByUsername(authenticationTokenDTO.getUsername())
                .orElseThrow(() -> new BusinessException(1001,
                    "User was not found or might be removed from our system"));
        String password = this.passwordEncoder.encode(changePasswordConfirmDTO.getNewPassword());
        boolean isSuccess = this.userDao
            .resetPassword(changePasswordConfirmDTO.getEmail(), password);
        log.info("Update password for user {}: {}", userDTO.getUsername(),
            isSuccess ? "Success" : "Failed");
    }

    @Override
    @Transactional
    public void updateOtherInfo(Map<String, Object> otherInfo) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
//            String infoJson = this.objectMapper.writeValueAsString(otherInfo);
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication
                .getPrincipal();
            String username = principal.getAttribute("username");
            if (username != null) {
                this.userDao.updateOtherInfo(username, otherInfo);
            } else {
                log.error("User id not found in oauth2 principal");
                throw new BusinessException(500, "User id not found in oauth2 principal");
            }
        } else {
            log.error("Principal is not of type oauth2");
            throw new BusinessException(500, "Principal is not of type oauth2");
        }
    }

    @Override
    @Transactional
    public void updateUser(String username, String password, boolean enabled, boolean accountLocked,
        boolean accountExpired,
        boolean credentialsExpired, String groups) {

        String newPassword = null;
        if (password != null && !password.trim().isEmpty()) {
            newPassword = this.passwordEncoder.encode(password);
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

    @Override
    @Transactional(readOnly = true)
    public List<String> getRoles(String username) {
        return this.userDao.findGroupListByUsername(username)
            .stream()
            .map(Group::getName)
            .collect(Collectors.toList());
    }
}
