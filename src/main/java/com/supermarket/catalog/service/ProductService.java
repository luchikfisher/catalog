package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;

import java.util.UUID;

public interface ProductService {

    UUID createProduct(CreateProductRequest request);

    Product getProduct(UUID productId);

    UUID updateProduct(UUID productId, UpdateProductRequest request);

    UUID increaseStock(UUID productId, StockUpdateRequest request);

    UUID decreaseStock(UUID productId, StockUpdateRequest request);

    UUID deleteProduct(UUID productId);
}