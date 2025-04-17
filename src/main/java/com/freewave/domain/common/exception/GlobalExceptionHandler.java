package com.freewave.domain.common.exception;

import com.freewave.domain.common.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request (잘못된 요청)
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleInvalidRequest(InvalidRequestException e, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(), URI.create(request.getRequestURI())));
    }

    // 401 Unauthorized (유효하지 않은 토큰)
    @ExceptionHandler({InvalidTokenException.class, JwtTokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleTokenException(RuntimeException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), URI.create(request.getRequestURI())));
    }

    // 403 Forbidden (계정 잠금, 접근 거부)
    @ExceptionHandler(AnomalyDetectionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionResponse> handleAnomalyDetection(AnomalyDetectionException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(HttpStatus.FORBIDDEN, e.getMessage(), URI.create(request.getRequestURI())));
    }

    // 500 Internal Server Error (기타 예외)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleAll(Exception e, HttpServletRequest request) {
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage(), URI.create(request.getRequestURI())));
    }
}
