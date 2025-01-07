package com.github.gerdanyjr.simple_transit.service;

import java.security.Principal;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;

public interface AuthService {

    User register(RegisterUserReq req);

    TokenRes login(LoginReq req);

    TokenRes refreshToken(String refreshToken);

    User updateUser(UpdateUserReq req, Principal principal);
}
