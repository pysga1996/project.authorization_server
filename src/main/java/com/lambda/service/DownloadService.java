package com.lambda.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface DownloadService {

    ResponseEntity<Resource> generateUrl(String fileName, String folder, HttpServletRequest request);
}
