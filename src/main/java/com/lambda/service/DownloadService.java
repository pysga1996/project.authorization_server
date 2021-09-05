package com.lambda.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface DownloadService {

    ResponseEntity<Resource> generateUrl(String fileName, String folder,
        HttpServletRequest request);
}
