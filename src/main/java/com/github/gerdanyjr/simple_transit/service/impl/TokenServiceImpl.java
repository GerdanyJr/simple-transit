package com.github.gerdanyjr.simple_transit.service.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.access-token.secret}")
    private String TOKENSECRET;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${jwt.access-token.expiration-time}")
    private Long ACCESSTOKENEXPIRATIONTIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private Long REFRESHTOKENEXPIRATIONTIME;

    @Override
    public String generateAccessToken(User user) {
        return JWT
                .create()
                .withIssuer(ISSUER)
                .withExpiresAt(getExpiresAt(ACCESSTOKENEXPIRATIONTIME))
                .withClaim("accessToken", "true")
                .withSubject(user.getLogin())
                .sign(Algorithm.HMAC256(TOKENSECRET));
    }

    @Override
    public String generateRefreshToken(User user) {
        return JWT
                .create()
                .withIssuer(ISSUER)
                .withExpiresAt(getExpiresAt(REFRESHTOKENEXPIRATIONTIME))
                .withClaim("refresh", "true")
                .withSubject(user.getLogin())
                .sign(Algorithm.HMAC256(TOKENSECRET));
    }

    @Override
    public String refreshToken(User user, String refreshToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }

    @Override
    public String getSubject(String accessToken) {
        return JWT
                .require(Algorithm.HMAC256(TOKENSECRET))
                .withIssuer(ISSUER)
                .withClaim("accessToken", "true")
                .build().verify(getToken(accessToken))
                .getSubject();
    }

    @Override
    public String getRefreshTokenSubject(String refreshToken) {
        return JWT
                .require(Algorithm.HMAC256(TOKENSECRET))
                .withIssuer(ISSUER)
                .withClaim("refreshToken", "true")
                .build().verify(getToken(refreshToken))
                .getSubject();
    }

    private Instant getExpiresAt(Long expirationTime) {
        return Instant.now().plusSeconds(expirationTime);
    }

    private String getToken(String token) {
        return token.replace("Bearer ", "");
    }

}
