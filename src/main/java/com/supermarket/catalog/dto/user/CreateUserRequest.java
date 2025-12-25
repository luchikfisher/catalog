package com.supermarket.catalog.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank String password,
        @Email @NotBlank String email
) {}