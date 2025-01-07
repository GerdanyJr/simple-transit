package com.github.gerdanyjr.simple_transit.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
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
                .withClaim("refreshToken", "true")
                .withSubject(user.getLogin())
                .sign(Algorithm.HMAC256(TOKENSECRET));
    }

    @Override
    public TokenRes refreshToken(User user, String refreshToken) {
        Map<String, String> claims = new HashMap<>();
        claims.put("refreshToken", "true");
        validateToken(refreshToken, claims);

        String accessToken = generateAccessToken(user);
        String newRefreshToken = generateAccessToken(user);

        return new TokenRes(accessToken, newRefreshToken);
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

    private void validateToken(String token, Map<String, String> claims) {
        Verification verification = JWT
                .require(Algorithm.HMAC256(TOKENSECRET))
                .withIssuer(ISSUER);

        claims.forEach((name, value) -> {
            verification.withClaim(name, value);
        });

        verification
                .build()
                .verify(getToken(token));
    }

    private Instant getExpiresAt(Long expirationTime) {
        return Instant.now().plusSeconds(expirationTime);
    }

    private String getToken(String token) {
        return token.replace("Bearer ", "");
    }

}
