package com.lambda.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lambda.model.dto.AuthenticationTokenDTO;
import com.lambda.model.dto.RegistrationDTO.RegistrationConfirmDTO;
import com.lambda.model.dto.ResetPasswordDTO.ChangePasswordConfirmDTO;
import com.lambda.model.dto.SearchResponseDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

@SuppressWarnings("deprecation")
public interface UserService {

    Map<String, UserProfileDTO> getUserStatusMap();

    Page<UserDTO> getUserList(Pageable pageable);

    UserDTO getCurrentUser();

    Map<String, Object> getUserShortInfo(String username);

    Optional<UserDTO> findByUsername(String username);

    UserDTO findByEmail(String email);

    void register(UserDTO user);

    void unregister(String username);

    void deleteById(Long id);

    SearchResponseDTO search(String searchText);

    void confirmRegistration(RegistrationConfirmDTO registrationConfirmDTO);

    void resetPassword(ChangePasswordConfirmDTO changePasswordConfirmDTO);

    void updateOtherInfo(Map<String, Object> otherInfo) throws JsonProcessingException;

    default void validateToken(AuthenticationTokenDTO authenticationTokenDTO) {
        if (authenticationTokenDTO == null) {
            throw new InvalidTokenException("Token not found");
        }
        Calendar cal = Calendar.getInstance();
        if ((authenticationTokenDTO.getExpireDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new InvalidTokenException("Token expired ");
        }
    }

    void updateUser(String username, String password, boolean enabled,
        boolean accountLocked, boolean accountExpired,
        boolean credentialsExpired, String groups);

    List<String> getRoles(String username);
}
