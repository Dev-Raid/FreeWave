package com.freewave.domain.common.exception;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}