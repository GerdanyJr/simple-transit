package com.github.gerdanyjr.simple_transit.service.impl;

import java.security.Principal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ConflictException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.UserService;
import com.github.gerdanyjr.simple_transit.util.Mapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User register(RegisterUserReq req) {
        userRepository
                .findByLogin(req.login())
                .ifPresent((foundUser) -> {
                    throw new ConflictException("Usuário já cadastrado com login: " + req.login());
                });

        User createdUser = Mapper.fromRegisterReqToUser(req, bCryptPasswordEncoder);

        return userRepository.save(createdUser);
    }

    @Override
    public User updateUser(UpdateUserReq req, Principal principal) {
        User user = (User) principal;

        user.setName(req.name());
        user.setPassword(bCryptPasswordEncoder.encode(req.password()));

        return userRepository.save(user);
    }

}
