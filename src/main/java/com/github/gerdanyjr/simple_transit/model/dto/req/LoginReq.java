package com.github.gerdanyjr.simple_transit.model.dto.req;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
    @NotBlank String login, 
    @NotBlank String password
) {
    
}
