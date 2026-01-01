package com.supermarket.catalog.controller;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.dto.user.UserResponse;
import com.supermarket.catalog.exception.UserAlreadyExistsException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users-catalog")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody @Valid CreateUserRequest request) throws UserAlreadyExistsException {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) throws EntityNotFoundException {
        User user = userService.getUser(id);
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .storeId(user.getStore().getId())
                .storeName(user.getStore().getName())
                .insertionTime(user.getInsertionTime())
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID update(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest request) throws UserAlreadyExistsException, EntityNotFoundException {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID delete(@PathVariable UUID id) throws EntityNotFoundException {
        return userService.deleteUser(id);
    }
}
