package com.supermarket.catalog.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String username,
        @Email @NotBlank String email
) {}