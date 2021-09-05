package com.lambda.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lambda.client.NotificationClient;
import com.lambda.model.dto.RegistrationDTO;
import com.lambda.model.dto.RegistrationDTO.RegistrationConfirmDTO;
import com.lambda.model.dto.ResetPasswordDTO;
import com.lambda.model.dto.ResetPasswordDTO.ChangePasswordConfirmDTO;
import com.lambda.model.dto.ResetPasswordDTO.ResetPasswordCase;
import com.lambda.model.dto.SearchResponseDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserService;
import java.util.Locale;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true",
    exposedHeaders = {HttpHeaders.SET_COOKIE})
@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    private final NotificationClient notificationClient;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService userService,
        NotificationClient notificationClient,
        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.notificationClient = notificationClient;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody RegistrationDTO registrationDTO,
        WebRequest request) {
        UserDTO userDTO = registrationDTO.getUserDTO();
        userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        String email = userDTO.getUserProfile().getEmail();
        String username = userDTO.getUsername();
        String clientRedirectUrl = registrationDTO.getRedirectUrl();
        this.userService.register(userDTO);
        Locale locale = request.getLocale();
        this.notificationClient.sendRegistrationEmail(username, email, clientRedirectUrl, locale);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("permitAll()")
    @PatchMapping("/registration-confirm")
    public ResponseEntity<Void> confirmRegistration(
        @RequestBody RegistrationConfirmDTO registrationConfirmDTO) {
        this.userService.confirmRegistration(registrationConfirmDTO);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,
        WebRequest request) {
        String email = resetPasswordDTO.getEmail();
        String clientRedirectUrl = resetPasswordDTO.getRedirectUrl();
        UserDTO user = this.userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Locale locale = request.getLocale();
        this.notificationClient
            .sendPasswordResetEmail(user.getUsername(), email, clientRedirectUrl, locale);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("permitAll()")
    @PatchMapping(value = "/reset-password")
    public ResponseEntity<Void> resetPassword(@Validated(ResetPasswordCase.class)
    @RequestBody ChangePasswordConfirmDTO changePasswordConfirmDTO) {
        this.userService.resetPassword(changePasswordConfirmDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<SearchResponseDTO> search(@RequestParam("name") String name) {
        SearchResponseDTO searchResponseDTO = this.userService.search(name);
        return new ResponseEntity<>(searchResponseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/delete-user", params = "id")
    public ResponseEntity<Void> deleteUser(@RequestParam("id") Long id) {
        this.userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/profile-map")
    public ResponseEntity<Map<String, UserProfileDTO>> getUserStatusMap() {
        Map<String, UserProfileDTO> userStatusMap = this.userService.getUserStatusMap();
        return new ResponseEntity<>(userStatusMap, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping(path = "/update-other-info")
    public ResponseEntity<Void> updateOtherInfo(@RequestBody Map<String, Object> otherInfo)
        throws JsonProcessingException {
        this.userService.updateOtherInfo(otherInfo);
        return ResponseEntity.ok().build();
    }

}
