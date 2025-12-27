package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.exception.ConflictException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.repository.StoreRepository;
import com.supermarket.catalog.repository.UserRepository;
import com.supermarket.catalog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final Clock clock;

    // ===== CREATE =====
    @Override
    public UUID createUser(CreateUserRequest request)
            throws ConflictException {

        String storeName = request.storeName().trim();

        if (userRepository.existsByUsername(request.username())) {
            log.warn("Attempt to create duplicate username: {}", request.username());
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Attempt to create duplicate email: {}", request.email());
            throw new ConflictException("Email already exists");
        }

        Store store = storeRepository.findByName(storeName)
                .orElseGet(() -> {
                    Store newStore = Store.builder()
                            .id(UUID.randomUUID())
                            .name(storeName)
                            .insertionTime(Instant.now(clock))
                            .build();

                    return storeRepository.save(newStore);
                });

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .store(store)
                .insertionTime(Instant.now(clock))
                .build();

        userRepository.save(user);
        log.info("User created: {}", user.getId());

        return user.getId();
    }

    // ===== READ =====
    @Override
    public User getUser(UUID userId)
            throws EntityNotFoundException {

        log.info("Fetching user {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: " + userId)
                );
    }

    // ===== UPDATE =====
    @Override
    public UUID updateUser(UUID userId, UpdateUserRequest request)
            throws ConflictException, EntityNotFoundException {

        User existing = getUser(userId);

        if (!existing.getUsername().equals(request.username())
                && userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username already exists");
        }

        User updated = User.builder()
                .id(existing.getId())
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .store(existing.getStore())
                .insertionTime(Instant.now(clock))
                .build();

        userRepository.save(updated);
        log.info("User updated: {}", userId);

        return userId;
    }

    // ===== DELETE =====
    @Override
    public UUID deleteUser(UUID userId)
            throws EntityNotFoundException {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);

        return userId;
    }
}
