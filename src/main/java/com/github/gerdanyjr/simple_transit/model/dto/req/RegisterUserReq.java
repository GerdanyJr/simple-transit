package com.github.gerdanyjr.simple_transit.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserReq(
                @NotBlank @Size(max = 50) String name,
                @NotBlank @Size(max = 20) String login,
                @NotBlank @Size(max = 50) String password) {

}
