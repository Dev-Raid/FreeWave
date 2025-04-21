package com.freewave.domain.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadFile(MultipartFile file);

    byte[] downloadFile(String imageUrl);

    void deleteFile(String imageUrl);

    boolean supportsContentType(String contentType);
}
