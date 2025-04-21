package com.freewave.domain.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileUploadResponse {
    private final String filename;
    private final String fileUrl;
}
