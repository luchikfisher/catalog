package com.supermarket.catalog.service;

import com.supermarket.catalog.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void creatingDuplicateUsernameShouldFail() {

        userService.createUser("test", "pass", "a@test.com");

        assertThatThrownBy(() ->
                userService.createUser("test", "pass", "b@test.com")
        ).isInstanceOf(ValidationException.class);
    }
}