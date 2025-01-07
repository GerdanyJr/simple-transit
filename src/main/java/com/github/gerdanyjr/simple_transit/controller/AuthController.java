package com.github.gerdanyjr.simple_transit.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserReq req) {
        User createdUser = authService.register(req);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@RequestBody @Valid LoginReq req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
