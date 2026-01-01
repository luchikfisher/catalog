package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.exception.UserAlreadyExistsException;
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

    @Override
    public UUID createUser(CreateUserRequest request) throws UserAlreadyExistsException {
        String storeName = request.storeName();
        if (userRepository.existsByUsername(request.username())) {
            log.warn("Attempt to create duplicate username: {}", request.username());
            throw new UserAlreadyExistsException("Username already exists");
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

    @Override
    public User getUser(UUID userId) throws EntityNotFoundException {
        log.info("Fetching user {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    @Override
    public UUID updateUser(UUID userId, UpdateUserRequest request) throws UserAlreadyExistsException, EntityNotFoundException {
        User existing = getUser(userId);
        if (!existing.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        userRepository.save(User.builder()
                .id(existing.getId())
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .store(existing.getStore())
                .insertionTime(Instant.now(clock))
                .build());
        log.info("User updated: {}", userId);
        return userId;
    }

    @Override
    public UUID deleteUser(UUID userId) throws EntityNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);
        return userId;
    }
}
