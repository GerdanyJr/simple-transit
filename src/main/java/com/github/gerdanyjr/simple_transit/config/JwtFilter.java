package com.github.gerdanyjr.simple_transit.config;

import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gerdanyjr.simple_transit.model.dto.res.ErrorResponse;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.BaseException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtFilter(TokenService tokenService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");

            if (token != null) {
                HashMap<String, String> claims = new HashMap<>();
                claims.put("accessToken", "true");
                String subject = tokenService.getSubject(token, claims);

                User user = userRepository
                        .findByLogin(subject)
                        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.apply(subject)));

                var authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities());

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (BaseException e) {
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    e.getTimestamp(),
                    e.getHttpStatus(),
                    e.getCode());

            response.setStatus(e.getCode());
            response.getWriter().write(toJson(error));
        } catch (Exception e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());

            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    Instant.now(),
                    HttpStatus.FORBIDDEN,
                    HttpStatus.FORBIDDEN.value());

            response.getWriter().write(toJson(error));
        }
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
