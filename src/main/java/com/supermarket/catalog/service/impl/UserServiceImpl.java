package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.exception.BusinessValidationException;
import com.supermarket.catalog.exception.ResourceNotFoundException;
import com.supermarket.catalog.repository.UserRepository;
import com.supermarket.catalog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // ===== CREATE =====
    @Override
    public UUID createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            log.warn("Attempt to create duplicate username: {}", request.username());
            throw new BusinessValidationException("Username already exists");
        }

        User user = new User(
                UUID.randomUUID(),
                request.username(),
                request.password(),
                request.email(),
                Instant.now()
        );

        userRepository.save(user);
        log.info("User created: {}", user.getId());

        return user.getId();
    }

    // ===== READ =====
    @Override
    public User getUser(UUID userId) {
        log.info("Get user request {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + userId)
                );
    }

    // ===== UPDATE =====
    @Override
    public UUID updateUser(UUID userId, UpdateUserRequest request) {

        User user = getUser(userId);

        if (!user.getUsername().equals(request.username())
                && userRepository.existsByUsername(request.username())) {
            throw new BusinessValidationException("Username already exists");
        }

        User updated = new User(
                user.getId(),
                request.username(),
                request.password(),
                request.email(),
                user.getJoinedAt()
        );

        userRepository.save(updated);
        log.info("User updated: {}", userId);

        return userId;
    }

    // ===== DELETE =====
    @Override
    public UUID deleteUser(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);

        return userId;
    }
}