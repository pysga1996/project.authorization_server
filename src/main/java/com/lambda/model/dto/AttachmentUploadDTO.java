package com.lambda.model.dto;

public class AttachmentUploadDTO extends UploadDTO {

    private String fileName;

    private String url;

    private String blobString;

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String createFileName(String ext) {
        return this.fileName.concat(".").concat(ext);
    }

    @Override
    public String getFolder() {
        return "attachment";
    }

    @Override
    public String getBlobString() {
        return this.blobString;
    }

    @Override
    public void setBlobString(String blobString) {
        this.blobString = blobString;
    }
}
