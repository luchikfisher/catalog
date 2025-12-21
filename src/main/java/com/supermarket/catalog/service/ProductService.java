package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.service.command.CreateProductCommand;
import com.supermarket.catalog.service.command.UpdateProductCommand;
import com.supermarket.catalog.service.command.UpdateStockCommand;

import java.util.UUID;

public interface ProductService {

    UUID createProduct(CreateProductCommand command);

    Product getProduct(UUID productId);

    UUID updateProduct(UUID productId, UpdateProductCommand command);

    UUID increaseStock(UUID productId, UpdateStockCommand command);

    UUID decreaseStock(UUID productId, UpdateStockCommand command);

    UUID deleteProduct(UUID productId);
}