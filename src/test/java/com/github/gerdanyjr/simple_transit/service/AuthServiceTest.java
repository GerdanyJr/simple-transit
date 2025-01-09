package com.github.gerdanyjr.simple_transit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private TokenService tokenService;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private Authentication authentication;

        @InjectMocks
        private AuthServiceImpl authService;

        private User user;

        private LoginReq loginReq;

        @BeforeEach
        void setup() {
                user = new User(1,
                                "teste",
                                "teste",
                                "teste",
                                List.of());

                loginReq = new LoginReq("login", "password");

                authentication = mock(Authentication.class);
        }

        @Test
        @DisplayName("should return tokenRes when valid credentials are passed")
        void givenValidCredentials_whenLogin_thenReturnTokenRes() {
                String accessToken = "accessToken";
                String refreshToken = "refreshToken";
                when(authenticationManager.authenticate(any(Authentication.class)))
                                .thenReturn(authentication);

                when(authentication.getPrincipal())
                                .thenReturn(user);

                when(tokenService.generateAccessToken(any(User.class)))
                                .thenReturn(accessToken);

                when(tokenService.generateRefreshToken(any(User.class)))
                                .thenReturn(refreshToken);

                TokenRes tokenRes = authService.login(loginReq);

                assertNotNull(tokenRes);
                assertEquals(accessToken, tokenRes.authToken());
                assertEquals(refreshToken, tokenRes.refreshToken());
        }

        @Test
        @DisplayName("should throw exception when invalid credentials are passed")
        void givenInvalidCredentials_whenLogin_thenThrowException() {
                when(authenticationManager
                                .authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new RuntimeException("Invalid credentials"));

                assertThrows(RuntimeException.class,
                                () -> authService.login(loginReq));
        }

        @Test
        @DisplayName("should return token when a valid refresh token is passed")
        void givenValidRefreshToken_whenRefreshToken_thenReturnToken() {
                String accessToken = "accessToken";
                String refreshToken = "refreshToken";
                when(tokenService.getSubject(anyString(), anyMap()))
                                .thenReturn("subject");

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(user));

                when(tokenService.refreshToken(any(User.class), anyString()))
                                .thenReturn(new TokenRes(accessToken, refreshToken));

                TokenRes tokenRes = authService.refreshToken("refreshToken");

                assertNotNull(tokenRes);
                assertEquals(accessToken, tokenRes.authToken());
                assertEquals(refreshToken, tokenRes.refreshToken());
        }

        @Test
        @DisplayName("should throw a NotFoundException when a refresh token with invalid user is passed")
        void givenRefreshTokenWithInvalidUser_whenRefreshToken_thenThrowNotFoundException() {
                when(tokenService.getSubject(anyString(), anyMap()))
                                .thenReturn("subject");

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> authService.refreshToken("refreshToken"));

                assertEquals(USER_NOT_FOUND.apply("subject"), e.getMessage());
        }

}
