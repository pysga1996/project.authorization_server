package com.lambda.model.dto;

public abstract class UploadDTO {

    public abstract String getUrl();

    public abstract String createFileName(String ext);

    public abstract String getFolder();

    public abstract String getBlobString();

    public abstract void setBlobString(String blobString);
}
