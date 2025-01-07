package com.github.gerdanyjr.simple_transit.model.dto.req;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserReq(
        @NotBlank String name,
        @NotBlank String password) {

}
