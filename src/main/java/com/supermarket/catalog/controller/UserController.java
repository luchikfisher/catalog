package com.supermarket.catalog.controller;

import com.supermarket.catalog.dto.user.*;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(
                request.username(),
                request.password(),
                request.email()
        );
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getJoinedAt()
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID id,
                       @RequestBody @Valid UpdateUserRequest request) {

        userService.updateUser(
                id,
                request.username(),
                request.password(),
                request.email()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}