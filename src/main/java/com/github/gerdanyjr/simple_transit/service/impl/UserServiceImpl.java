package com.github.gerdanyjr.simple_transit.service.impl;

import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.LOGIN_ALREADY_REGISTERED;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;

import java.net.URI;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.UserRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ConflictException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.UserService;
import com.github.gerdanyjr.simple_transit.util.Mapper;
import com.github.gerdanyjr.simple_transit.util.UriUtil;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public URI register(RegisterUserReq req) {
        userRepository
                .findByLogin(req.login())
                .ifPresent((foundUser) -> {
                    throw new ConflictException(LOGIN_ALREADY_REGISTERED.apply(req.login()));
                });

        ;

        User createdUser = userRepository
                .save(Mapper
                        .fromRegisterReqToUser(req, bCryptPasswordEncoder));

        return UriUtil.createUri("/{id}", createdUser.getId().toString());
    }

    @Override
    public URI updateUser(UpdateUserReq req, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        user.setName(req.name());
        user.setPassword(bCryptPasswordEncoder.encode(req.password()));

        User createdUser = userRepository.save(user);

        return UriUtil.createUri("/{id}", createdUser.getId().toString());
    }

    @Override
    public UserRes findById(Integer id) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        USER_NOT_FOUND.apply(id.toString())));

        return Mapper.fromUserToUserRes(foundUser);
    }

}
