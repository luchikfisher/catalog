package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.exception.UnauthorizedException;

import java.util.UUID;

public interface ProductService {

    UUID createProduct(User user, CreateProductRequest request)
            throws InvalidInputException, UnauthorizedException;

    Product getProduct(User user, UUID productId)
            throws EntityNotFoundException, UnauthorizedException;

    UUID updateProduct(User user, UUID productId, UpdateProductRequest request)
            throws InvalidInputException, EntityNotFoundException, UnauthorizedException;

    UUID increaseStock(User user, UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException, UnauthorizedException;

    UUID decreaseStock(User user, UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException, UnauthorizedException;

    UUID deleteProduct(User user, UUID productId)
            throws EntityNotFoundException, UnauthorizedException;
}
