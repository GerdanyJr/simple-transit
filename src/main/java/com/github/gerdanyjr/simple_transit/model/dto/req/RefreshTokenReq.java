package com.github.gerdanyjr.simple_transit.model.dto.req;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenReq(@NotBlank String refreshToken) {

}
