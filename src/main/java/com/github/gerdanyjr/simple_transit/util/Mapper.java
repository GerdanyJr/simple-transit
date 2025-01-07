package com.github.gerdanyjr.simple_transit.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.entity.User;

public class Mapper {

    public static User fromRegisterReqToUser(RegisterUserReq req, BCryptPasswordEncoder encoder) {
        User user = new User();
        user.setLogin(req.login());
        user.setPassword(encoder.encode(req.password()));
        user.setName(req.name());

        return user;
    }

}
