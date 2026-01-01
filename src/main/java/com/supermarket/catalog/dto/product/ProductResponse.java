package com.supermarket.catalog.dto.product;

import com.supermarket.catalog.domain.product.Category;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        String name,
        Category category,
        BigDecimal price,
        int stockQuantity,
        String supplier,
        String description,
        UUID storeId,
        String storeName,
        Instant insertionTime
) {
}
