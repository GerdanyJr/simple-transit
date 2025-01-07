package com.github.gerdanyjr.simple_transit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.gerdanyjr.simple_transit.model.entity.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(null,
                "teste",
                "teste",
                "teste",
                List.of());
    }

    @Test
    @DisplayName("should return a user when a existing login is passed")
    void givenValidLogin_whenFindByLogin_thenReturnUser() {
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByLogin(user.getLogin());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getLogin(), foundUser.get().getLogin());
    }

    @Test
    @DisplayName("should return empty when a non-existing login is passed")
    void givenInvalidLogin_whenFindByLogin_thenReturnEmpty() {
        Optional<User> foundUser = userRepository.findByLogin("");

        assertTrue(foundUser.isEmpty());
    }
}
