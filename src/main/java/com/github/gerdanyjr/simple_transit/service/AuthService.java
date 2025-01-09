package com.github.gerdanyjr.simple_transit.service;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;

public interface AuthService {

    TokenRes login(LoginReq req);

    TokenRes refreshToken(String refreshToken);

}
