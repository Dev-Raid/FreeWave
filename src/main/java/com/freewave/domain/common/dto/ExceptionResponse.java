package com.freewave.domain.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private final String timestamp = String.valueOf(LocalDateTime.now());
    private final HttpStatus status;
    private final String error;
    private final URI path;

    public ExceptionResponse(HttpStatus status, String error, URI path) {
        this.status = status;
        this.error = error;
        this.path = path;
    }
}