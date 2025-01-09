package com.github.gerdanyjr.simple_transit.service;

import java.net.URI;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;

public interface UserService {

    URI register(RegisterUserReq req);

    URI updateUser(UpdateUserReq req);
}
