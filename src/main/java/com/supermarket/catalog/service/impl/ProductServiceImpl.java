package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.repository.ProductRepository;
import com.supermarket.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Clock clock;

    // ===== CREATE =====
    @Override
    public UUID createProduct(CreateProductRequest request)
            throws InvalidInputException {

        if (request.price() == null || request.price().signum() <= 0) {
            throw new InvalidInputException("Product price must be positive");
        }

        if (request.initialQuantity() != null && request.initialQuantity() < 0) {
            throw new InvalidInputException("Initial quantity cannot be negative");
        }

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .category(request.category())
                .price(request.price())
                .stockQuantity(
                        request.initialQuantity() != null
                                ? request.initialQuantity()
                                : 0
                )
                .supplier(request.supplier())
                .description(request.description())
                .insertionTime(Instant.now(clock))
                .build();

        productRepository.save(product);
        log.info("Product created: {} with initial quantity {}", product.getId(), product.getStockQuantity());

        return product.getId();
    }

    // ===== READ =====
    @Override
    @Transactional(readOnly = true)
    public Product getProduct(UUID productId)
            throws EntityNotFoundException {

        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found: " + productId)
                );
    }

    // ===== UPDATE PRODUCT =====
    @Override
    public UUID updateProduct(UUID productId, UpdateProductRequest request)
            throws EntityNotFoundException {

        Product existing = getProduct(productId);

        Product updated = Product.builder()
                .id(existing.getId())
                .name(request.name())
                .category(request.category())
                .price(request.price())
                .stockQuantity(existing.getStockQuantity())
                .supplier(request.supplier())
                .description(request.description())
                .insertionTime(Instant.now(clock))
                .build();

        productRepository.save(updated);
        log.info("Product updated: {}", productId);

        return productId;
    }

    // ===== INCREASE STOCK =====
    @Override
    public UUID increaseStock(UUID productId, StockUpdateRequest request)
            throws EntityNotFoundException {

        Product product = getProduct(productId);

        Product updated = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity() + request.amount())
                .supplier(product.getSupplier())
                .description(product.getDescription())
                .insertionTime(product.getInsertionTime())
                .build();

        productRepository.save(updated);
        log.info("Stock increased for product {} by {}", productId, request.amount());

        return productId;
    }

    // ===== DECREASE STOCK =====
    @Override
    public UUID decreaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException {

        Product product = getProduct(productId);

        int newStock = product.getStockQuantity() - request.amount();
        if (newStock < 0) {
            log.warn("Attempt to reduce stock zero for product {}", productId);
            throw new InvalidInputException("Stock cannot be negative");
        }

        Product updated = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(newStock)
                .supplier(product.getSupplier())
                .description(product.getDescription())
                .insertionTime(product.getInsertionTime())
                .build();

        productRepository.save(updated);
        log.info("Stock decreased for product {} by {}", productId, request.amount());

        return productId;
    }

    // ===== DELETE =====
    @Override
    public UUID deleteProduct(UUID productId)
            throws EntityNotFoundException {

        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Product not found: " + productId);
        }

        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);

        return productId;
    }
}