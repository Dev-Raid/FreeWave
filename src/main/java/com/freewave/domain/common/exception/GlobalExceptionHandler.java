package com.freewave.domain.common.exception;

import com.freewave.domain.auth.exception.AnomalyDetectionException;
import com.freewave.domain.common.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request (잘못된 요청)
    @ExceptionHandler({InvalidRequestException.class, FileUploadException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleInvalidRequest(RuntimeException e,
            HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                        URI.create(request.getRequestURI())));
    }

    // 401 Unauthorized (유효하지 않은 토큰)
    @ExceptionHandler({InvalidTokenException.class, JwtTokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleTokenException(RuntimeException e,
            HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage(),
                        URI.create(request.getRequestURI())));
    }

    // 403 Forbidden (계정 잠금, 접근 거부)
    @ExceptionHandler(AnomalyDetectionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionResponse> handleAnomalyDetection(AnomalyDetectionException e,
            HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(HttpStatus.FORBIDDEN, e.getMessage(),
                        URI.create(request.getRequestURI())));
    }

    // 404 Not Found (찾을 수 없음)
    @ExceptionHandler({FileDownloadException.class, FileDeleteException.class,
            ServiceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleNotFound(RuntimeException e,
            HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage(),
                        URI.create(request.getRequestURI())));
    }

    // 415 Unsupported Media Type (지원되지 않는 미디어 유형)
    @ExceptionHandler(UnsupportedFileTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ExceptionResponse> handleUnsupportedMediaType(
            UnsupportedFileTypeException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ExceptionResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(),
                        URI.create(request.getRequestURI())));
    }

    // 422 Unprocessable Entity (처리할 수 없는 엔티티)
    @ExceptionHandler(FileProcessingException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ExceptionResponse> handleUnprocessableEntity(FileProcessingException e,
            HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(),
                        URI.create(request.getRequestURI())));
    }

    // 500 Internal Server Error (기타 예외)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleAll(Exception e, HttpServletRequest request) {
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal server error: " + e.getMessage(),
                        URI.create(request.getRequestURI())));
    }
}