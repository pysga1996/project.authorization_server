package com.lambda.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.StorageService;
import com.lambda.service.UserProfileService;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true",
        exposedHeaders = {HttpHeaders.SET_COOKIE})
public class UserProfileRestController {

    private final UserService userService;

    private final UserProfileService userProfileService;

    private final StorageService storageService;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserProfileRestController(UserProfileService userProfileService,
                                     StorageService storageService,
                                     UserService userService, ObjectMapper objectMapper) {
        this.userProfileService = userProfileService;
        this.storageService = storageService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<UserProfileDTO> getCurrentProfile(@RequestParam(name = "username", required = false) String username) {
        UserProfileDTO userProfileDTO = this.userProfileService.getProfileByUsername(username);
        if (userProfileDTO != null) {
            return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("#oauth2.user")
    @PostMapping
    public ResponseEntity<Void> createProfile(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        this.userProfileService.createProfile(userProfileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.user")
    @PutMapping
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        this.userProfileService.updateProfile(userProfileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    public ResponseEntity<UserProfileDTO> uploadAvatar(@RequestParam(value = "avatar", required = false) MultipartFile multipartFile,
                                                    @RequestParam("userProfile") String profileJsonStr) throws IOException {
        UserProfileDTO userProfileDTO = this.objectMapper.readValue(profileJsonStr, UserProfileDTO.class);
        UserDTO userDTO = this.userService.getCurrentUser();
        userProfileDTO.setUsername(userDTO.getUsername());
        if (multipartFile != null) {
            String fileDownloadUri = this.storageService.upload(multipartFile, userProfileDTO);
            userProfileDTO.setAvatarUrl(fileDownloadUri);
        }
        if (userDTO.getUserProfile() == null) {
            this.userProfileService.createProfile(userProfileDTO);
        } else {
            this.userProfileService.updateProfile(userProfileDTO);
        }
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }
}
