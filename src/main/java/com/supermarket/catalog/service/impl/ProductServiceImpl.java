package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.exception.BusinessValidationException;
import com.supermarket.catalog.exception.ResourceNotFoundException;
import com.supermarket.catalog.repository.ProductRepository;
import com.supermarket.catalog.service.ProductService;
import com.supermarket.catalog.service.command.CreateProductCommand;
import com.supermarket.catalog.service.command.UpdateProductCommand;
import com.supermarket.catalog.service.command.UpdateStockCommand;
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
    public UUID createProduct(CreateProductCommand command) {

        validatePrice(command.price());

        int quantity = command.initialQuantity() == null ? 0 : command.initialQuantity();
        if (quantity < 0) {
            throw new BusinessValidationException("Initial quantity cannot be negative");
        }

        Product product = new Product(
                UUID.randomUUID(),
                command.name(),
                command.category(),
                command.price(),
                quantity,
                command.supplier(),
                command.description(),
                Instant.now()
        );

        productRepository.save(product);
        log.info("Product created: {} with initial quantity {}", product.getId(), quantity);

        return product.getId();
    }

    // ===== READ =====
    @Override
    @Transactional(readOnly = true)
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + productId)
                );
    }

    // ===== UPDATE PRODUCT =====
    @Override
    public UUID updateProduct(UUID productId, UpdateProductCommand command) {

        validatePrice(command.price());

        Product existing = getProduct(productId);

        Product updated = new Product(
                existing.getId(),
                command.name(),
                command.category(),
                command.price(),
                existing.getStockQuantity(),
                command.supplier(),
                command.description(),
                existing.getCreatedAt()
        );

        productRepository.save(updated);
        log.info("Product updated: {}", productId);

        return productId;
    }

    // ===== INCREASE STOCK =====
    @Override
    public UUID increaseStock(UUID productId, UpdateStockCommand command) {

        validateStockAmount(command.amount());

        Product product = getProduct(productId);

        Product updated = new Product(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity() + command.amount(),
                product.getSupplier(),
                product.getDescription(),
                product.getCreatedAt()
        );

        productRepository.save(updated);
        log.info("Stock increased for product {} by {}", productId, command.amount());

        return productId;
    }

    // ===== DECREASE STOCK =====
    @Override
    public UUID decreaseStock(UUID productId, UpdateStockCommand command) {

        validateStockAmount(command.amount());

        Product product = getProduct(productId);

        int newStock = product.getStockQuantity() - command.amount();
        if (newStock < 0) {
            throw new BusinessValidationException("Stock cannot be negative");
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
        log.info("Stock decreased for product {} by {}", productId, command.amount());

        return productId;
    }

    // ===== DELETE =====
    @Override
    public UUID deleteProduct(UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found: " + productId);
        }

        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);

        return productId;
    }

    // ===== VALIDATION =====
    private void validatePrice(BigDecimal price) {
        if (price == null || price.signum() <= 0) {
            throw new BusinessValidationException("Price must be positive");
        }
    }

    private void validateStockAmount(int amount) {
        if (amount <= 0) {
            throw new BusinessValidationException("Stock amount must be positive");
        }
    }
}