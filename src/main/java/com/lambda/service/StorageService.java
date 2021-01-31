package com.lambda.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String uploadAvatar(MultipartFile multipartFile, String username) throws IOException;
}
