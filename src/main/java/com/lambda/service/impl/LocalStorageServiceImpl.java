package com.lambda.service.impl;

import com.lambda.config.properties.LocalStorageProperty;
import com.lambda.error.FileNotFoundException;
import com.lambda.error.FileStorageException;
import com.lambda.model.dto.UploadDTO;
import com.lambda.service.StorageService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Log4j2
@Service
@ConditionalOnProperty(prefix = "storage", name = "storage-type", havingValue = "local")
public class LocalStorageServiceImpl extends StorageService {

    private final Path storageLocation;

    @Autowired
    public LocalStorageServiceImpl(LocalStorageProperty localStorageProperty) {
        this.storageLocation = Paths.get(localStorageProperty.getUploadDir())
            .toAbsolutePath().normalize();
        Path audioPath = storageLocation.resolve("audio");
        Path coverPath = storageLocation.resolve("cover");
        Path avatarPath = storageLocation.resolve("avatar");
        try {
            Files.createDirectories(this.storageLocation);
            Files.createDirectories(audioPath);
            Files.createDirectories(coverPath);
            Files.createDirectories(avatarPath);
        } catch (Exception ex) {
            throw new FileStorageException(
                "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile, UploadDTO uploadDTO) {
        String ext = getExtension(multipartFile);
        String folder = uploadDTO.getFolder();
        String fileName = uploadDTO.createFileName(ext);
        String rootUri = "/api/resource/download";
        this.normalizeFileName(fileName);
        try {
            // Check if the file's title contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException(
                    "Sorry! Filename contains invalid path sequence " + fileName);
            }
            String blobString = folder + "/" + fileName;
            // Copy file to the target location (Replacing existing file with the same title)
            Path targetLocation = this.storageLocation.resolve(blobString);
            uploadDTO.setBlobString(blobString);
            Files.copy(multipartFile.getInputStream(), targetLocation,
                StandardCopyOption.REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(rootUri)
                .path("/")
                .path(folder)
                .path("/")
                .path(fileName).toUriString();
        } catch (IOException ex) {
            throw new FileStorageException(
                "Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void delete(UploadDTO uploadDTO) {
        Path filePath = storageLocation.resolve(uploadDTO.getBlobString()).normalize();
        File file = filePath.toFile();
        log.info("Delete file {} success? {}", uploadDTO.getBlobString(), file.delete());
    }

    @Override
    public Resource loadFileAsResource(String fileName, String folder) {
        try {
            Path filePath = storageLocation.resolve(folder + "/" + fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + folder + "/" + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + folder + "/" + fileName, ex);
        }
    }
}
