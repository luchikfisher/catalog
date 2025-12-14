package com.supermarket.catalog.service;

import com.supermarket.catalog.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void creatingDuplicateUsernameShouldFail() {

        String username = "test_" + UUID.randomUUID();

        userService.createUser(username, "pass", "a@test.com");

        assertThatThrownBy(() ->
                userService.createUser(username, "pass", "b@test.com")
        ).isInstanceOf(ValidationException.class);
    }
}