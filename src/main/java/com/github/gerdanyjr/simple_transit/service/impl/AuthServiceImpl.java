package com.github.gerdanyjr.simple_transit.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;
import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.AuthService;
import com.github.gerdanyjr.simple_transit.service.TokenService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenService tokenService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public TokenRes login(LoginReq req) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                req.login(),
                req.password());

        Authentication authenticate = authenticationManager.authenticate(authToken);

        User user = (User) authenticate.getPrincipal();

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new TokenRes(accessToken, refreshToken);
    }

    @Override
    public TokenRes refreshToken(String refreshToken) {
        Map<String, String> claims = new HashMap<>();
        claims.put("refreshToken", "true");
        String subject = tokenService.getSubject(refreshToken, claims);

        User user = userRepository
                .findByLogin(subject)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.apply(subject)));

        return tokenService.refreshToken(user, refreshToken);
    }

}
