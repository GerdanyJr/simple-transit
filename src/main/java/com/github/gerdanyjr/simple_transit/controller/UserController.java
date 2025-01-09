package com.github.gerdanyjr.simple_transit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.UserRes;
import com.github.gerdanyjr.simple_transit.service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/usuarios")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserReq req) {
        return ResponseEntity
                .created(userService.register(req))
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserRes> findById(@PathVariable("userId") Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserReq req, Authentication authentication) {
        return ResponseEntity
                .noContent()
                .location(userService.updateUser(req, authentication))
                .build();
    }
}
