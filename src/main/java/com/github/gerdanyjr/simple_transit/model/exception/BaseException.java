package com.github.gerdanyjr.simple_transit.model.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {
    private Instant timestamp;
    private HttpStatus httpStatus;
    private Integer code;

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.timestamp = Instant.now();
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
    }

}
