package com.github.gerdanyjr.simple_transit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ConflictException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private RegisterUserReq req;

    @BeforeEach
    void setup() {
        user = new User(1,
                "teste",
                "teste",
                "teste",
                List.of());

        req = new RegisterUserReq("teste",
                "teste",
                "teste");
    }

    @Test
    @DisplayName("should return created user when a non existent user is passed")
    void givenNonExistingUser_whenRegister_thenReturnCreatedUser() {
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        when(userRepository.findByLogin(anyString()))
                .thenReturn(Optional.empty());

        User createdUser = userService.register(req);

        assertNotNull(createdUser);
        assertEquals(createdUser, user);
    }

    @Test
    @DisplayName("shouldn throw exception when existent user is passed")
    void givenExistingUser_whenRegister_thenThrowException() {
        when(userRepository.findByLogin(anyString()))
                .thenReturn(Optional.of(user));

        assertThrows(
                ConflictException.class,
                () -> userService.register(req));
    }

}
