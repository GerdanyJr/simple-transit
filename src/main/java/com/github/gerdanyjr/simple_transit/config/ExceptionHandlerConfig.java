package com.github.gerdanyjr.simple_transit.config;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.gerdanyjr.simple_transit.model.dto.res.ErrorResponse;
import com.github.gerdanyjr.simple_transit.model.dto.res.ValidationErrorResponse;
import com.github.gerdanyjr.simple_transit.model.exception.BaseException;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponse(e.getMessage(),
                        e.getTimestamp(),
                        e.getHttpStatus(),
                        e.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getFieldErrors()
                .forEach(error -> errors
                        .put(error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(errors,
                        Instant.now(),
                        HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.value()));
    }
}
