package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.exception.ResourceNotFoundException;
import com.supermarket.catalog.exception.ValidationException;
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

    @Override
    public UUID createUser(String username, String password, String email) {

        if (userRepository.existsByUsername(username)) {
            log.warn("Attempt to create duplicate username: {}", username);
            throw new ValidationException("Username already exists");
        }

        User user = new User(
                UUID.randomUUID(),
                username,
                password,
                email,
                Instant.now()
        );

        userRepository.save(user);
        log.info("User created: {}", user.getId());

        return user.getId();
    }

    @Override
    public User getUser(UUID userId) {
        log.info("Get user request {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId)
                );
    }

    @Override
    public void updateUser(UUID userId, String username, String email) {

        User user = getUser(userId);

        if (!user.getUsername().equals(username)
                && userRepository.existsByUsername(username)) {
            throw new ValidationException("Username already exists");
        }

        User updated = new User(
                user.getId(),
                username,
                user.getPassword(),
                email,
                user.getJoinedAt()
        );

        userRepository.save(updated);
        log.info("User updated: {}", userId);
    }

    @Override
    public void deleteUser(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);
    }
}