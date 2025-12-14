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
            String description
    );

    Product getProduct(UUID productId);

    void updateProduct(
            UUID productId,
            String name,
            Category category,
            BigDecimal price,
            String supplier,
            String description
    );

    void increaseStock(UUID productId, int amount);

    void decreaseStock(UUID productId, int amount);

    void deleteProduct(UUID productId);
}