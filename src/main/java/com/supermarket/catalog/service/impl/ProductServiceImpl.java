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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // ===== CREATE =====
    @Override
    public UUID createProduct(CreateProductRequest request)
            throws InvalidInputException {

        validatePrice(request.price());

        int quantity = request.initialQuantity() == null ? 0 : request.initialQuantity();
        if (quantity < 0) {
            throw new InvalidInputException("Initial quantity cannot be negative");
        }

        Product product = new Product(
                UUID.randomUUID(),
                request.name(),
                request.category(),
                request.price(),
                quantity,
                request.supplier(),
                request.description(),
                Instant.now()
        );

        productRepository.save(product);
        log.info("Product created: {} with initial quantity {}", product.getId(), quantity);

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
            throws InvalidInputException, EntityNotFoundException {

        validatePrice(request.price());

        Product existing = getProduct(productId);

        Product updated = new Product(
                existing.getId(),
                request.name(),
                request.category(),
                request.price(),
                existing.getStockQuantity(),
                request.supplier(),
                request.description(),
                existing.getCreatedAt()
        );

        productRepository.save(updated);
        log.info("Product updated: {}", productId);

        return productId;
    }

    // ===== INCREASE STOCK =====
    @Override
    public UUID increaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException {

        validateStockAmount(request.amount());

        Product product = getProduct(productId);

        Product updated = new Product(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity() + request.amount(),
                product.getSupplier(),
                product.getDescription(),
                product.getCreatedAt()
        );

        productRepository.save(updated);
        log.info("Stock increased for product {} by {}", productId, request.amount());

        return productId;
    }

    // ===== DECREASE STOCK =====
    @Override
    public UUID decreaseStock(UUID productId, StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException {

        validateStockAmount(request.amount());

        Product product = getProduct(productId);

        int newStock = product.getStockQuantity() - request.amount();
        if (newStock < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }

        Product updated = new Product(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                newStock,
                product.getSupplier(),
                product.getDescription(),
                product.getCreatedAt()
        );

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

    // ===== VALIDATION =====
    private void validatePrice(BigDecimal price)
            throws InvalidInputException {

        if (price == null || price.signum() <= 0) {
            throw new InvalidInputException("Price must be positive");
        }
    }

    private void validateStockAmount(int amount)
            throws InvalidInputException {

        if (amount <= 0) {
            throw new InvalidInputException("Stock amount must be positive");
        }
    }
}