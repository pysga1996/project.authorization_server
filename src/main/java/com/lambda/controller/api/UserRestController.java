package com.lambda.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lambda.client.NotificationClient;
import com.lambda.model.dto.*;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;

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
    public ResponseEntity<String> createUser(@Valid @RequestBody RegistrationDTO registrationDTO, WebRequest request) {
        UserDTO userDTO = registrationDTO.getUserDTO();
        userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        String email = userDTO.getUserProfile().getEmail();
        String username = userDTO.getUsername();
        String clientRedirectUrl = registrationDTO.getRedirectUrl();
        this.userService.register(userDTO);
        Locale locale = request.getLocale();
        this.notificationClient.sendRegistrationEmail(username, email, clientRedirectUrl, locale);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/registration-confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();
        String url = this.userService.confirmRegistration(token);
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, WebRequest request) {
        String email = resetPasswordDTO.getEmail();
        String clientRedirectUrl = resetPasswordDTO.getRedirectUrl();
        UserDTO user = this.userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Locale locale = request.getLocale();
        this.notificationClient.sendPasswordResetEmail(user.getUsername(), email, clientRedirectUrl, locale);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/reset-password", params = {"token"})
    public ResponseEntity<Void> showChangePasswordPage(@RequestParam("token") String token) throws UnknownHostException {
        HttpHeaders headers = new HttpHeaders();
        String url = this.userService.showChangePasswordPage(token);
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PreAuthorize("permitAll()")
    @PutMapping(value = "/reset-password", params = {"token"})
    public ResponseEntity<String> savePassword(@Valid @RequestBody PasswordDTO passwordDto,
                                               @RequestParam("token") String token) {
        UserDTO userDTO = this.userService.checkResetPassToken(token);
        userDTO.setPassword(this.passwordEncoder.encode(passwordDto.getNewPassword()));
        this.userService.register(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
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
