package com.supermarket.catalog.service;

import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.exception.UserAlreadyExistsException;
import com.supermarket.catalog.test_config.TestEnvironment;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.Arguments;

class UserServiceIntegrationTest extends TestEnvironment {

    @Autowired
    private UserService userService;

    @ParameterizedTest
    @MethodSource("duplicateUserScenarios")
    void creatingDuplicateUserShouldFail(
            CreateUserRequest firstUser,
            CreateUserRequest duplicateUser
    ) {

        assertDoesNotThrow(() -> userService.createUser(firstUser));
        assertThatThrownBy(() -> userService.createUser(duplicateUser)).isInstanceOf(UserAlreadyExistsException.class);
    }

    static Stream<Arguments> duplicateUserScenarios() {

        return Stream.of(arguments(
                        CreateUserRequest.builder()
                                .username("duplicate_user")
                                .password("password")
                                .email("user1@test.com")
                                .storeName("store-1")
                                .build(),
                        CreateUserRequest.builder()
                                .username("duplicate_user")
                                .password("password")
                                .email("user2@test.com")
                                .storeName("store-2")
                                .build()
                )
        );
    }
}