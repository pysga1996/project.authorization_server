package com.lambda.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DownloadService {

    private static final Logger logger = LoggerFactory.getLogger(DownloadService.class);

//    public ResponseEntity<Resource> generateUrl(String fileName, HttpServletRequest request, StorageService storageService) {
//
//        Path path = Paths.get("");
////        if (storageService instanceof AudioStorageService) {
////            path = ((AudioStorageService) storageService).audioStorageLocation;
////        } else if (storageService instanceof CoverStorageService) {
////            path = ((CoverStorageService) storageService).coverStorageLocation;
////        } else if (storageService instanceof AvatarStorageService) {
////            path = ((AvatarStorageService) storageService).avatarStorageLocation;
////        }
//        // Load file as Resource
//        Resource resource = storageService.loadFileAsResource(path, fileName);
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            logger.info("Could not determine file type.");
//        }
//        // Fallback to the default content type if type could not be determined
//        if (contentType == null) {
//            contentType = "application/octet-stream";
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
}
