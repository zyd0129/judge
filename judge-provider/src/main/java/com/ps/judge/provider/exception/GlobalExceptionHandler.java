package com.ps.judge.provider.exception;

import com.ps.jury.api.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e) {
        log.error("handle runtime exception: {}", e.getMessage());
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse handleHttpMessageConversionException(NullPointerException e) {
        log.error("handle nullPointerException exception: {}", e.getMessage());
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
