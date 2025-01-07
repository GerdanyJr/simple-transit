package com.github.gerdanyjr.simple_transit.service;

import com.github.gerdanyjr.simple_transit.model.entity.User;

public interface TokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String refreshToken(User user, String refreshToken);

    String getSubject(String accessToken);

    String getRefreshTokenSubject(String refreshToken);
}
