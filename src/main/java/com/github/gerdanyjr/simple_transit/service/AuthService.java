package com.github.gerdanyjr.simple_transit.service;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.LoginRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;

public interface AuthService {

    LoginRes login(LoginReq req);

    TokenRes refreshToken(String refreshToken);

}
