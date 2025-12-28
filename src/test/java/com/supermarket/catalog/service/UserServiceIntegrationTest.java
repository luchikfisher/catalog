package com.supermarket.catalog.service;

import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.exception.ConflictException;
import com.supermarket.catalog.testinfra.BaseIntegrationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.Arguments;

class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @ParameterizedTest
    @MethodSource("duplicateUserScenarios")
    void creatingDuplicateUserShouldFail(
            CreateUserRequest firstUser,
            CreateUserRequest duplicateUser
    ) {

        assertDoesNotThrow(() -> userService.createUser(firstUser));

        assertThatThrownBy(() -> userService.createUser(duplicateUser))
                .isInstanceOf(ConflictException.class);
    }

    static Stream<Arguments> duplicateUserScenarios() {

        // scenario: duplicate username
        String username = "user_" + UUID.randomUUID();
        String email1 = UUID.randomUUID() + "@test.com";
        String email2 = UUID.randomUUID() + "@test.com";
        String storeName1 = "store-" + UUID.randomUUID();
        String storeName2 = "store-" + UUID.randomUUID();

        // scenario: duplicate email
        String username1 = "user_" + UUID.randomUUID();
        String username2 = "user_" + UUID.randomUUID();
        String email = UUID.randomUUID() + "@test.com";
        String storeName3 = "store-" + UUID.randomUUID();
        String storeName4 = "store-" + UUID.randomUUID();

        return Stream.of(
                arguments(
                        CreateUserRequest.builder()
                                .username(username)
                                .password("password")
                                .email(email1)
                                .storeName(storeName1)
                                .build(),
                        CreateUserRequest.builder()
                                .username(username) // duplicate username
                                .password("password")
                                .email(email2)
                                .storeName(storeName2)
                                .build()
                ),
                arguments(
                        CreateUserRequest.builder()
                                .username(username1)
                                .password("password")
                                .email(email)
                                .storeName(storeName3)
                                .build(),
                        CreateUserRequest.builder()
                                .username(username2)
                                .password("password")
                                .email(email) // duplicate email
                                .storeName(storeName4)
                                .build()
                )
        );
    }
}