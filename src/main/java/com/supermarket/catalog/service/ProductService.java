package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.product.Product;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {

    UUID createProduct(
            String name,
            Category category,
            BigDecimal price,
            String supplier,
            String description,
            Integer initialQuantity
    );

    Product getProduct(UUID productId);

    UUID updateProduct(
            UUID productId,
            String name,
            Category category,
            BigDecimal price,
            String supplier,
            String description
    );

    UUID increaseStock(UUID productId, int amount);

    UUID decreaseStock(UUID productId, int amount);

    UUID deleteProduct(UUID productId);
}