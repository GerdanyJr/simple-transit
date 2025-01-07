package com.github.gerdanyjr.simple_transit.model.exception.impl;

import org.springframework.http.HttpStatus;

import com.github.gerdanyjr.simple_transit.model.exception.BaseException;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
