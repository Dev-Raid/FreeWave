package com.freewave.domain.common.service;

import com.freewave.domain.common.exception.UnsupportedFileTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ServiceFactory {

    private final List<S3Service> s3Services;

    public S3Service getServiceForContentType(String contentType) {
        return s3Services.stream()
                .filter(service -> service.supportsContentType(contentType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileTypeException("Unsupported file format: " + contentType));
    }
}
