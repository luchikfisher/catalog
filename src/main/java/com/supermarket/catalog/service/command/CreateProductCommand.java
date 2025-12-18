package com.supermarket.catalog.service.command;

import com.supermarket.catalog.domain.product.Category;

import java.math.BigDecimal;

public record CreateProductCommand(
        String name,
        Category category,
        BigDecimal price,
        String supplier,
        String description,
        Integer initialQuantity
) {}