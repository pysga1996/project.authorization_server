package com.lambda.controller;

import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.UserProfileService;
import com.lambda.service.UserService;
import com.lambda.service.impl.AvatarStorageService;
import com.lambda.service.impl.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/user-profile")
public class UserProfileRestController {

    private final UserService userService;

    private final UserProfileService userProfileService;

    private final AvatarStorageService avatarStorageService;

    private final DownloadService downloadService;

    @Autowired
    public UserProfileRestController(UserService userService, UserProfileService userProfileService, AvatarStorageService avatarStorageService, DownloadService downloadService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.avatarStorageService = avatarStorageService;
        this.downloadService = downloadService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/current-profile")
    public ResponseEntity<UserProfileDTO> getProfile() {
        UserProfileDTO userProfileDTO = this.userProfileService.getCurrentProfile();
        if (userProfileDTO != null) {
            return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileDTO> getCurrentProfile(@PathVariable("id") Long id) {
        UserProfileDTO userProfileDTO = this.userProfileService.getProfileById(id);
        if (userProfileDTO != null) {
            return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        this.userProfileService.updateProfile(userProfileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PreAuthorize("permitAll()")
//    @GetMapping("/avatar/{fileName:.+}")
//    public ResponseEntity<Resource> getAvatar(@PathVariable("fileName") String fileName, HttpServletRequest request) {
//        return downloadService.generateUrl(fileName, request, avatarStorageService);
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile multipartFile) {
        UserProfileDTO userProfileDTO = this.userProfileService.getCurrentProfile();
        UserDTO userDTO = this.userService.getCurrentUser();
        if (multipartFile != null) {
            String fileDownloadUri = avatarStorageService.saveToFirebaseStorage(userDTO, multipartFile);
            userProfileDTO.setAvatarUrl(fileDownloadUri);
        }
        userProfileService.updateProfile(userProfileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
