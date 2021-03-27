package com.lambda.service;

import com.lambda.error.FileStorageException;
import com.lambda.model.dto.UploadDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class StorageService {

    protected void normalizeFileName(String fileName) {
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
    }

    protected String getExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return originalFileName != null ?
                originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : "";
    }

    public abstract String upload(MultipartFile multipartFile, UploadDTO uploadDTO) throws IOException;

    public abstract void delete(UploadDTO uploadDTO);

    public Resource loadFileAsResource(String fileName, String folder) {
        return null;
    }
}
