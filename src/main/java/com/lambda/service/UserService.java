package com.lambda.service;

import com.lambda.model.dto.AuthenticationTokenDTO;
import com.lambda.model.dto.SearchResponseDTO;
import com.lambda.model.dto.UserDTO;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Optional;

@SuppressWarnings("deprecation")
public interface UserService {

    UserDTO getCurrentUser();

    Optional<UserDTO> findByUsername(String username);

    UserDTO findByEmail(String email);

    void save(UserDTO user, boolean createAction);

    void deleteById(Long id);

    SearchResponseDTO search(String searchText);

    String confirmRegistration(String token);

    String showChangePasswordPage(String token) throws UnknownHostException;

    UserDTO checkResetPassToken(String token);



    default void validateToken(AuthenticationTokenDTO authenticationTokenDTO) {
        if (authenticationTokenDTO == null) {
            throw new InvalidTokenException("Token not found");
        }
        Calendar cal = Calendar.getInstance();
        if ((authenticationTokenDTO.getExpireDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new InvalidTokenException("Expired token");
        }
    }
}
