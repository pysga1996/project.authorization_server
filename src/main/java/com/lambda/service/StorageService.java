package com.lambda.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.gson.JsonIOException;
import com.lambda.error.FileNotFoundException;
import com.lambda.error.FileStorageException;
import com.lambda.model.dto.UserDTO;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

public abstract class StorageService<T> {

    private void normalizeFileName(String fileName) {
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
    }

    private String getOldExtension(String url) {
        return url.substring(url.lastIndexOf(".") + 1);
    }

    private String getNewExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : "";
    }

//    public String getOldFileName(Object object) {
//        UserDTO user = (UserDTO) object;
//        String avatarUrl = user.getAvatarUrl();
//        String oldExtension = getOldExtension(avatarUrl);
//        return user.getId().toString().concat(" - ").concat(user.getUsername()).concat(".").concat(oldExtension);
//    }

    private String getNewFileName(Object object, MultipartFile file) {
        String extension = getNewExtension(file);
        UserDTO user = (UserDTO) object;
        return user.getId().toString().concat(" - ").concat(user.getUsername()).concat(".").concat(extension);
    }

//    public void deleteOldFile(Path storageLocation, Object object, MultipartFile file) {
//        String newExtension = getNewExtension(file);
//        // check if new image ext is different from old file ext
//        String url = "";
//        if (object instanceof Song) {
//            url = ((Song) object).getUrl();
//        } else if (object instanceof Album) {
//            url = ((Album) object).getCoverUrl();
//        } else if (object instanceof User) {
//            url = ((User) object).getAvatarUrl();
//        } else if (object instanceof Artist) {
//            url = ((Artist) object).getAvatarUrl();
//        }
//        if (url != null && !url.equals("")) {
//            String oldFileName = getOldFileName(object);
//            String oldExtension = getOldExtension(url);
//            if (!oldExtension.equals(newExtension)) {
//                deleteLocalStorageFile(storageLocation, oldFileName.concat(".").concat(oldExtension));
//            }
//        }
//    }

    public Resource loadFileAsResource(Path storageLocation, String fileName) {
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    private StorageClient getFirebaseStorage() {
        try {
//            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/Users/nguyenxuanhoang/Documents/ThucHanhCodeGym/adminsdk/climax-sound-firebase-adminsdk-c29fo-27166cf850.json"))
//                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
//            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/mnt/D43C7B5B3C7B3816/CodeGym/Module 4/Project Climax Sound/climax-sound-firebase-adminsdk-c29fo-27166cf850.json"))
//                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

//            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            JSONObject json = new JSONObject();
            json.put("type", "service_account");
            json.put("project_id", "climax-sound");
            json.put("type", "service_account");
            json.put("project_id", "climax-sound");
            json.put("private_key_id", "27166cf8509ebafcfc6b452dbcfdf8c28c4038e3");
            json.put("private_key",
                    "-----BEGIN PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC9wR44zBRs9gLe\n" +
                            "wXZSrZZjbWS8RReANwr+PsfRzyNnZjMHnkm6hemdtpEYJ0s5dNre2dL2d0cGNsFy\n" +
                            "K1ClV4ZSrjA2pKamhPsK418WED1RYotp8lWeeT4vN37obBtEL5rlMJO763a0oGJ2\n" +
                            "zJotubqpVCxIoW462tOvLzo1/moXy5fQij4meiXhwYCq8IvFUjHaLxhs/5fDdNTW\n" +
                            "D+pZhndJLjOOkBPPdUAE3RsdTWAF9hKFcD5RKvMTVU0lXc38N2JRcLWzqOzLQmhc\n" +
                            "BJUEE8arfgkfJ6slyJi9YeUgCX8dZsCtEvJ38bKlaPvbVkahfvoJsK2yEQ8OLfve\n" +
                            "yhgI0A3dAgMBAAECggEAArzxpFzVgp5Te5wLtV+0htTCRQaI3fHCt9kiEFX5KyR0\n" +
                            "t2HCVe2uvvbZGyHIr8M5kUj6sVHBSvFT/e4VaNKy09NC9iw5Yg0TLkdfojWJHPkw\n" +
                            "t85RC/YF6VqB+0qWuMzTDbGTatJtURKWDnl0qWo/q50qUmtPPYwrd8tU/13KHB5u\n" +
                            "mFkZglVfvRgArjzeUhRprdfm0V1c1HjkskLx82tNFhl6YLF1yHQbddom5++o3Tcm\n" +
                            "YWa8mFrDS/q/AY+E9mG76nFZISB9wFETFBwGhNt8G9PyMMATbJBUTF8c42oKSL+W\n" +
                            "8IPyuRCpbYIPEALn16VwmvKBL/dVqO3kBoG7Uy8AgQKBgQDdjjC+MjxFPdvs74nN\n" +
                            "d/vsSgDKg4dA/QuuimhRwEaKW1QSbtDHyeWj6Aj7vP1KMPo8D2Yd0zVK5a6GTYGi\n" +
                            "28OQlothXd20udGDEHBhVdX3M62psn6GyEBJnUY9lEtw8INsnPxI/pDn0rJ2UIy9\n" +
                            "6W1EZF3VNUusXTDKTS5SILLfPQKBgQDbQUJZCQcVqabVcvk52XSoCCDFOOtWXlpl\n" +
                            "PQsRr+vHX2WYdclGRR3vgHbLKXoCSNfaSEITBasJKWYU0H+3HBzud7BL0IcIkM+3\n" +
                            "lf7fEiHBk8W4TI4GJMAcw69kFkY2kXH8akPOSXLi9d0UGKWo38lz2Vwxmn92pwEW\n" +
                            "B4XNDurTIQKBgQDYlSGtLGqivZ/6dQffJkXNbWJsljtoGnaK/56PHElZA4KiKlZK\n" +
                            "mGG24yO1A8EP/+T/RYZr7YaRWwpLHAV4cSEyfRhv7FTJUUUwvKc/X6u05n4EAAuU\n" +
                            "Mro1vFtn40oEc/nl+Wje6ow5M01mP8P1+vToAL2BqYH6U3dYrC519qUleQKBgQCL\n" +
                            "KqrwVJiYmWx5w1aJ74rqNO6IAI7v7Ue3Rr3F5kHj9JtJ4El3Ptu3b3T3k0YMOSw5\n" +
                            "MvAtnsVnNU3EHGtUUJXUj0fpd1yehTwzLRPvh1TByAgR6RWktKL0HwkuoCzE6iDN\n" +
                            "tK2BPIGdqa0/2j0cKZ921xp7qbMKHwrDbtbwx20pwQKBgQCafGyQlG7LWSSev0c8\n" +
                            "6Ve2MqH8znNbNtNHY6ksiAP9PJSMvsUvHdyRLjk1G61wcP9fW6klp9DrfHjzhjwb\n" +
                            "SJ3gKPKlOWTTVpYr5xgCusrVq0t4bgMaEwQu3HUM5h+LLPy4dtrF7XSSlWKKN1O/\n" +
                            "5gdnHTyLhi9qmMEPMMTCkERfrw==\n-----END PRIVATE KEY-----\n");
            json.put("client_email", "firebase-adminsdk-c29fo@climax-sound.iam.gserviceaccount.com");
            json.put("client_id", "100170251038112588007");
            json.put("auth_uri", "https://accounts.google.com/o/oauth2/auth");
            json.put("token_uri", "https://oauth2.googleapis.com/token");
            json.put("auth_provider_x509_cert_url", "https://www.googleapis.com/oauth2/v1/certs");
            json.put("client_x509_cert_url", "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-c29fo%40climax-sound.iam.gserviceaccount.com");

            GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(json.toString().getBytes()));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://climax-sound.firebaseio.com")
                    .setStorageBucket("climax-sound.appspot.com")
                    .build();

            FirebaseApp fireApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            if (firebaseApps != null && !firebaseApps.isEmpty()) {
                for (FirebaseApp app : firebaseApps) {
                    if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                        fireApp = app;
                }
            } else
                fireApp = FirebaseApp.initializeApp(options);
            return StorageClient.getInstance(fireApp);
        } catch (IOException ex) {
            throw new FileStorageException("Could not get admin-sdk json file. Please try again!", ex);
        } catch (JSONException ex) {
            throw new JsonIOException("Could not get admin-sdk json file. Please try again!", ex);
        }
    }

    public String saveToFirebaseStorage(Object object, MultipartFile file) {
        String fileName = getNewFileName(object, file);
        normalizeFileName(fileName);
        StorageClient storageClient = getFirebaseStorage();
        Bucket bucket = storageClient.bucket();
        try {
            InputStream testFile = file.getInputStream();
            String blobString = "avatar/" + fileName;;
            Blob blob = bucket.create(blobString, testFile, Bucket.BlobWriteOption.userProject("climax-sound"));
            bucket.getStorage().updateAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            String blobName = blob.getName();
//            ((UserDTO) object).setAvatarBlobString(blobName);
            return blob.getMediaLink();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

//    public String saveToLocalStorage(Path storageLocation, Object object, MultipartFile file) {
//        String fileName = getNewFileName(object, file);
//        String rootUri = "";
//        if (object instanceof Song) {
//            rootUri = "/api/song/download/";
//        } else if (object instanceof Album) {
//            rootUri = "/api/album/download/";
//        } else if (object instanceof User | object instanceof Artist) {
//            rootUri = "/api/avatar/download/";
//        }
//        normalizeFileName(fileName);
//        try {
//            // Check if the file's title contains invalid characters
//            if (fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
//            // Copy file to the target location (Replacing existing file with the same title)
//            Path targetLocation = storageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return ServletUriComponentsBuilder.fromCurrentContextPath().path(rootUri).path(fileName).toUriString();
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//    }

    public void deleteFirebaseStorageFile(Object object) {
        StorageClient storageClient = getFirebaseStorage();
//        String blobString = ((UserDTO) object).getAvatarBlobString();;
//        BlobId blobId = BlobId.of(storageClient.bucket().getName(), blobString);
//        storageClient.bucket().getStorage().delete(blobId);
    }


    public Boolean deleteLocalStorageFile(Path storageLocation, String fileName) {
        Path filePath = storageLocation.resolve(fileName).normalize();
        File file = filePath.toFile();
        return file.delete();
    }
}
