package com.github.gerdanyjr.simple_transit.service;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.entity.User;

public interface UserService {

    User register(RegisterUserReq req);

}
