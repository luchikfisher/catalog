package com.supermarket.catalog.dto.product;

import jakarta.validation.constraints.Positive;

public record StockUpdateRequest(@Positive int amount) {}