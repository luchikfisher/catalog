package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.ResourceNotFoundException;

import java.util.UUID;

public interface ProductService {

    UUID createProduct(CreateProductRequest request)
            throws InvalidInputException;

    Product getProduct(UUID productId)
            throws ResourceNotFoundException;

    UUID updateProduct(UUID productId, UpdateProductRequest request)
            throws InvalidInputException, ResourceNotFoundException;

    UUID increaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, ResourceNotFoundException;

    UUID decreaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, ResourceNotFoundException;

    UUID deleteProduct(UUID productId)
            throws ResourceNotFoundException;
}