package com.github.gerdanyjr.simple_transit.model.dto.res;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;

public record ValidationErrorResponse(
        Map<String, String> errors,
        Instant timestamp,
        HttpStatus httpStatus,
        Integer code) {

}
