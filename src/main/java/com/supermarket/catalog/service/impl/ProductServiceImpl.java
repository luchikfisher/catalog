package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.exception.UnauthorizedException;
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
    public UUID createProduct(User user, CreateProductRequest request)
            throws InvalidInputException, UnauthorizedException {

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
                .store(getStoreForUser(user))
                .insertionTime(Instant.now(clock))
                .build();

        productRepository.save(product);
        log.info("Product created: {} with initial quantity {}", product.getId(), product.getStockQuantity());

        return product.getId();
    }

    // ===== READ =====
    @Override
    @Transactional(readOnly = true)
    public Product getProduct(User user, UUID productId)
            throws EntityNotFoundException, UnauthorizedException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found: " + productId)
                );

        validateStoreAccess(user, product);

        return product;
    }

    // ===== UPDATE PRODUCT =====
    @Override
    public UUID updateProduct(User user, UUID productId, UpdateProductRequest request)
            throws EntityNotFoundException, UnauthorizedException {

        Product existing = getProduct(user, productId);

        Product updated = Product.builder()
                .id(existing.getId())
                .name(request.name())
                .category(request.category())
                .price(request.price())
                .stockQuantity(existing.getStockQuantity())
                .supplier(request.supplier())
                .description(request.description())
                .store(existing.getStore())
                .insertionTime(Instant.now(clock))
                .build();

        productRepository.save(updated);
        log.info("Product updated: {}", productId);

        return productId;
    }

    // ===== INCREASE STOCK =====
    @Override
    public UUID increaseStock(User user, UUID productId, StockUpdateRequest request)
            throws EntityNotFoundException, UnauthorizedException {

        Product product = getProduct(user, productId);

        Product updated = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity() + request.amount())
                .supplier(product.getSupplier())
                .description(product.getDescription())
                .store(product.getStore())
                .insertionTime(product.getInsertionTime())
                .build();

        productRepository.save(updated);
        log.info("Stock increased for product {} by {}", productId, request.amount());

        return productId;
    }

    // ===== DECREASE STOCK =====
    @Override
    public UUID decreaseStock(User user, UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException, UnauthorizedException {

        Product product = getProduct(user, productId);

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
                .store(product.getStore())
                .insertionTime(product.getInsertionTime())
                .build();

        productRepository.save(updated);
        log.info("Stock decreased for product {} by {}", productId, request.amount());

        return productId;
    }

    // ===== DELETE =====
    @Override
    public UUID deleteProduct(User user, UUID productId)
            throws EntityNotFoundException, UnauthorizedException {

        getProduct(user, productId);
        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);

        return productId;
    }

    private void validateStoreAccess(User user, Product product) throws UnauthorizedException {
        if (user == null || user.getStore() == null) {
            throw new UnauthorizedException("User has no store assigned");
        }

        if (!user.getStore().getId().equals(product.getStore().getId())) {
            throw new UnauthorizedException("User is not allowed to access this store");
        }
    }

    private Store getStoreForUser(User user)
            throws UnauthorizedException {
        if (user == null || user.getStore() == null) {
            throw new UnauthorizedException("User has no store assigned");
        }

        return user.getStore();
    }
}
