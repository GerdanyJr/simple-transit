package com.github.gerdanyjr.simple_transit.service;

import java.security.Principal;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.entity.User;

public interface UserService {

    User register(RegisterUserReq req);

    User updateUser(UpdateUserReq req, Principal principal);
}
