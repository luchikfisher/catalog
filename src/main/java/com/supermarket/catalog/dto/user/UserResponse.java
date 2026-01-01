package com.supermarket.catalog.dto.user;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String email,
        UUID storeId,
        String storeName,
        Instant insertionTime
) {
}
