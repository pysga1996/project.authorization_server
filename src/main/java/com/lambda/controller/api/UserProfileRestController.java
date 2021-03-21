package com.lambda.controller.api;

import com.lambda.error.UserNotFoundException;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.service.StorageService;
import com.lambda.service.UserProfileService;
import com.lambda.service.UserService;
import com.lambda.service.impl.CloudStorageServiceImpl;
import com.lambda.service.impl.DownloadService;
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

    @Autowired
    public UserProfileRestController(UserProfileService userProfileService, CloudStorageServiceImpl storageService, DownloadService downloadService, UserService userService) {
        this.userProfileService = userProfileService;
        this.storageService = storageService;
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public ResponseEntity<UserProfileDTO> getProfile() {
        UserProfileDTO userProfileDTO = this.userProfileService.getCurrentProfile();
        if (userProfileDTO != null) {
            return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getCurrentProfile(@PathVariable("id") Long id) {
        UserProfileDTO userProfileDTO = this.userProfileService.getProfileById(id);
        if (userProfileDTO != null) {
            return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("#oauth2.user")
    @PostMapping("")
    public ResponseEntity<Void> createProfile(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        this.userProfileService.createProfile(userProfileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.user")
    @PutMapping("")
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
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile multipartFile) throws IOException {
        UserDTO userDTO = this.userService.getCurrentUser();
        if (userDTO == null) {
            throw new UserNotFoundException();
        }
        UserProfileDTO userProfileDTO;
        if (userDTO.getUserProfile() == null) {
            userProfileDTO = new UserProfileDTO();
            userProfileDTO.setUserId(userDTO.getId());
            this.userProfileService.createProfile(userProfileDTO);
        } else {
            userProfileDTO = userDTO.getUserProfile();
        }
        String username = userDTO.getUsername();
        String fileDownloadUri = this.storageService.uploadAvatar(multipartFile, username);
        userProfileDTO.setAvatarUrl(fileDownloadUri);
        userProfileService.updateProfile(userProfileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
