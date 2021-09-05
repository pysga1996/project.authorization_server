package com.lambda.controller.api;

import com.lambda.service.DownloadService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/resource")
public class ResourceRestController {

    private final DownloadService downloadService;

    @Autowired
    public ResourceRestController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/download/{folder}/{fileName:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("fileName") String fileName,
        @PathVariable("folder") String folder,
        HttpServletRequest request) {
        return this.downloadService.generateUrl(fileName, folder, request);
    }
}
