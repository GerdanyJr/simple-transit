package com.github.gerdanyjr.simple_transit.model.exception.impl;

import org.springframework.http.HttpStatus;

import com.github.gerdanyjr.simple_transit.model.exception.BaseException;

public class ArgumentNotValidException extends BaseException {

    public ArgumentNotValidException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
