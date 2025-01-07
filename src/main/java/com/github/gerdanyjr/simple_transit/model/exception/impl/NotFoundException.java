package com.github.gerdanyjr.simple_transit.model.exception.impl;

import org.springframework.http.HttpStatus;

import com.github.gerdanyjr.simple_transit.model.exception.BaseException;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
