package com.supermarket.catalog.dto.product;

import com.supermarket.catalog.domain.product.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateProductRequest(
        @NotBlank String name,
        @NotNull Category category,
        @NotNull @Positive BigDecimal price,
        @NotBlank String supplier,
        @PositiveOrZero Integer initialQuantity,
        String description
) {}