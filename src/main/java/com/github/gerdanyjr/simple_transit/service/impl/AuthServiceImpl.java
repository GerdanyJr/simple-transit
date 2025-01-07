package com.github.gerdanyjr.simple_transit.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ConflictException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.AuthService;
import com.github.gerdanyjr.simple_transit.service.TokenService;
import com.github.gerdanyjr.simple_transit.util.Mapper;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenService tokenService,
            UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User register(RegisterUserReq req) {
        userRepository
                .findByLogin(req.login())
                .ifPresent((foundUser) -> {
                    throw new ConflictException("Usuário já cadastrado com login: " + req.login());
                });

        User createdUser = Mapper.fromRegisterReqToUser(req, bCryptPasswordEncoder);

        return userRepository.save(createdUser);
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

}
