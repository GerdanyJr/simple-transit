package com.github.gerdanyjr.simple_transit.config;

import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.repository.UserRepository;

@Service
public class UserDetailsServiceConfig implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.apply(username)));
    }

}
