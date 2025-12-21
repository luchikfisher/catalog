package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.exception.ConflictException;
import com.supermarket.catalog.exception.EntityNotFoundException;

import java.util.UUID;

public interface UserService {

    UUID createUser(CreateUserRequest request)
            throws ConflictException;

    User getUser(UUID userId)
            throws EntityNotFoundException;

    UUID updateUser(UUID userId, UpdateUserRequest request)
            throws ConflictException, EntityNotFoundException;

    UUID deleteUser(UUID userId)
            throws EntityNotFoundException;
}