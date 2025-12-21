package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;

import java.util.UUID;

public interface UserService {

    UUID createUser(CreateUserRequest request);

    User getUser(UUID userId);

    UUID updateUser(UUID userId, UpdateUserRequest request);

    UUID deleteUser(UUID userId);
}