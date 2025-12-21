package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.EntityNotFoundException;

import java.util.UUID;

public interface ProductService {

    UUID createProduct(CreateProductRequest request)
            throws InvalidInputException;

    Product getProduct(UUID productId)
            throws EntityNotFoundException;

    UUID updateProduct(UUID productId, UpdateProductRequest request)
            throws InvalidInputException, EntityNotFoundException;

    UUID increaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException;

    UUID decreaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException;

    UUID deleteProduct(UUID productId)
            throws EntityNotFoundException;
}