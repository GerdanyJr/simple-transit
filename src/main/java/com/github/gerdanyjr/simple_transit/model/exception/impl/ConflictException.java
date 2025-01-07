package com.github.gerdanyjr.simple_transit.model.exception.impl;

import org.springframework.http.HttpStatus;

import com.github.gerdanyjr.simple_transit.model.exception.BaseException;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
