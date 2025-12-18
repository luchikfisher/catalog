package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.exception.ResourceNotFoundException;
import com.supermarket.catalog.exception.BusinessValidationException;
import com.supermarket.catalog.repository.ProductRepository;
import com.supermarket.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public UUID createProduct(
            String name,
            Category category,
            BigDecimal price,
            String supplier,
            String description,
            Integer initialQuantity
    ) {

        validatePrice(price);

        int quantity = Objects.requireNonNullElse(initialQuantity, 0);

        if (quantity < 0) {
            throw new BusinessValidationException("Initial quantity cannot be negative");
        }

        Product product = new Product(
                UUID.randomUUID(),
                name,
                category,
                price,
                quantity,
                supplier,
                description,
                Instant.now()
        );

        productRepository.save(product);
        log.info("Product created: {} with initial quantity {}", product.getId(), quantity);

        return product.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    @Override
    public UUID updateProduct(
            UUID productId,
            String name,
            Category category,
            BigDecimal price,
            String supplier,
            String description
    ) {

        validatePrice(price);

        Product existing = getProduct(productId);

        Product updated = new Product(
                existing.getId(),
                name,
                category,
                price,
                existing.getStockQuantity(),
                supplier,
                description,
                existing.getCreatedAt()
        );

        productRepository.save(updated);
        log.info("Product updated: {}", productId);
        return productId;
    }

    @Override
    public UUID increaseStock(UUID productId, int amount) {

        Product product = getProduct(productId);

        Product updated = new Product(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity() + amount,
                product.getSupplier(),
                product.getDescription(),
                product.getCreatedAt()
        );

        productRepository.save(updated);
        log.info("Stock increased for product {} by {}", productId, amount);
        return productId;
    }

    @Override
    public UUID decreaseStock(UUID productId, int amount) {

        Product product = getProduct(productId);

        int newStock = product.getStockQuantity() - amount;
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
        log.info("Stock decreased for product {} by {}", productId, amount);
        return productId;
    }

    @Override
    public UUID deleteProduct(UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found: " + productId);
        }

        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);
        return productId;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.signum() <= 0) {
            throw new BusinessValidationException("Price must be positive");
        }
    }
}