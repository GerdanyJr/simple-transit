package com.github.gerdanyjr.simple_transit.model.dto.res;

public record LoginRes(
        Integer userId,
        String username,
        String authToken,
        String refreshToken) {

}
