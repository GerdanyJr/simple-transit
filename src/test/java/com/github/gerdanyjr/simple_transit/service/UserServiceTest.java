package com.github.gerdanyjr.simple_transit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.LOGIN_ALREADY_REGISTERED;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.UserRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ConflictException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private BCryptPasswordEncoder encoder;

        @Mock
        private Authentication authentication;

        @Mock
        private ServletUriComponentsBuilder uriBuilder;

        @InjectMocks
        private UserServiceImpl userService;

        private User user;

        private User updatedUser;

        private RegisterUserReq registerUserReq;

        private UpdateUserReq updateUserReq;

        @BeforeAll
        static void setupMockRequest() {
                MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                mockRequest.setRequestURI("/usuarios");
                ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
                RequestContextHolder.setRequestAttributes(attributes);
        }

        @BeforeEach
        void setup() {
                user = new User(1,
                                "teste",
                                "teste",
                                "teste",
                                List.of());

                updatedUser = new User(1,
                                "teste2",
                                "teste2",
                                "teste2",
                                List.of());

                registerUserReq = new RegisterUserReq("teste",
                                "teste",
                                "teste");

                updateUserReq = new UpdateUserReq(
                                "teste2",
                                "teste2");
        }

        @Test
        @DisplayName("should return created user location when a inexistent user is passed")
        void givenInexistentUser_whenRegister_thenReturnCreatedUser() {
                when(userRepository.save(any(User.class)))
                                .thenReturn(user);

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.empty());

                URI uri = userService.register(registerUserReq);

                assertNotNull(uri);
                assertEquals("/usuarios/1", uri.getPath());
        }

        @Test
        @DisplayName("should throw exception when existent user is passed")
        void givenExistingUser_whenRegister_thenThrowException() {
                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(user));

                ConflictException e = assertThrows(
                                ConflictException.class,
                                () -> userService.register(registerUserReq));

                assertEquals(LOGIN_ALREADY_REGISTERED.apply(user.getLogin()), e.getMessage());

        }

        @Test
        @DisplayName("should return updated user when updateUser")
        void whenUpdateUser_thenReturnUpdatedUser() {
                when(authentication.getPrincipal())
                                .thenReturn(user);

                when(encoder.encode(anyString()))
                                .thenReturn("hashedPassword");

                when(userRepository.save(any(User.class)))
                                .thenReturn(updatedUser);

                URI uri = userService.updateUser(updateUserReq, authentication);
                assertNotNull(uri);
                assertEquals("/usuarios/1", uri.getPath());
        }

        @Test
        @DisplayName("should throw NotFoundException when user does not exist")
        void givenNonExistentUserId_whenFindById_thenThrowNotFoundException() {
                when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

                NotFoundException e = assertThrows(
                                NotFoundException.class,
                                () -> userService.findById(1));

                assertEquals(USER_NOT_FOUND.apply("1"), e.getMessage());
        }

        @Test
        @DisplayName("should return UserRes when user exists")
        void givenExistentUserId_whenFindById_thenReturnUserRes() {
                when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

                UserRes result = userService.findById(1);

                assertNotNull(result);
        }

}
