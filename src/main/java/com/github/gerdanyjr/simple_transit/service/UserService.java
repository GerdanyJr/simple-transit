package com.github.gerdanyjr.simple_transit.service;

import java.net.URI;

import org.springframework.security.core.Authentication;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.UserRes;

public interface UserService {

    URI register(RegisterUserReq req);

    URI updateUser(UpdateUserReq req, Authentication authentication);

    UserRes findById(Integer id);
}
