package com.supermarket.catalog.dto.product;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

public record StockUpdateRequest(@Positive int amount) {}