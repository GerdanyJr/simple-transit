package com.github.gerdanyjr.simple_transit.model.dto.res;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
                String message,
                Instant timestamp,
                HttpStatus httpStatus,
                Integer code) {

}
