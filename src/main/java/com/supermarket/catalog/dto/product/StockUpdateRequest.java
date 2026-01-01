package com.supermarket.catalog.dto.product;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record StockUpdateRequest(@Positive int amount) {

    public static StockUpdateRequest of(int amount) {
        return new StockUpdateRequest(amount);
    }
}