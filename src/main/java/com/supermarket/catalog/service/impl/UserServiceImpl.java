package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.exception.NotFoundException;
import com.supermarket.catalog.exception.ValidationException;
import com.supermarket.catalog.repository.UserRepository;
import com.supermarket.catalog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
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
            throw new NotFoundException("User not found: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);
    }
}