package com.ps.judge.web.exception;

import com.ps.common.ApiResponse;
import com.ps.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ApiResponse handleNullPointerException(NullPointerException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ApiResponse handleIOException(IOException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse handBadCredentials(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse handle(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public ApiResponse handle(BizException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(600, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handle(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(600, e.getMessage());
    }
}
