package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.user.User;

import java.util.UUID;

public interface UserService {

    UUID createUser(String username, String password, String email);

    User getUser(UUID userId);

    void updateUser(UUID userId, String username, String email);

    void deleteUser(UUID userId);
}