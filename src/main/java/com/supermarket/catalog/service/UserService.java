package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.exception.UserAlreadyExistsException;

import java.util.UUID;

public interface UserService {
    UUID createUser(CreateUserRequest request) throws UserAlreadyExistsException;

    User getUser(UUID userId) throws EntityNotFoundException;

    UUID updateUser(UUID userId, UpdateUserRequest request) throws UserAlreadyExistsException, EntityNotFoundException;

    UUID deleteUser(UUID userId) throws EntityNotFoundException;
}