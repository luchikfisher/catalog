package com.supermarket.catalog.dto.product;

import com.supermarket.catalog.domain.product.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank String name,
        @NotNull Category category,
        @NotNull @Positive BigDecimal price,
        @NotBlank String supplier,
        String description
) {
}