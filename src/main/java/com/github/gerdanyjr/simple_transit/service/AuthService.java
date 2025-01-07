package com.github.gerdanyjr.simple_transit.service;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;

public interface AuthService {

    User register(RegisterUserReq req);

    TokenRes login(LoginReq req);
}
