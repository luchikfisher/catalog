package com.supermarket.catalog.service.impl;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.exception.NotFoundException;
import com.supermarket.catalog.exception.ValidationException;
import com.supermarket.catalog.repository.ProductRepository;
import com.supermarket.catalog.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public UUID createProduct(
            String name,
            Category category,
            BigDecimal price,
            String supplier,
            String description
    ) {

        validatePrice(price);

        Product product = new Product(
                UUID.randomUUID(),
                name,
                category,
                price,
                0,
                supplier,
                description,
                Instant.now()
        );

        productRepository.save(product);
        log.info("Product created: {}", product.getId());

        return product.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
    }

    @Override
    public void updateProduct(
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
    }

    @Override
    public void increaseStock(UUID productId, int amount) {

        validateStockAmount(amount);

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
    }

    @Override
    public void decreaseStock(UUID productId, int amount) {

        validateStockAmount(amount);

        Product product = getProduct(productId);

        int newStock = product.getStockQuantity() - amount;
        if (newStock < 0) {
            log.warn("Attempt to reduce stock below zero for product {}", productId);
            throw new ValidationException("Stock cannot be negative");
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
    }

    @Override
    public void deleteProduct(UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found: " + productId);
        }

        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);
    }

    private void validateStockAmount(int amount) {
        if (amount <= 0) {
            throw new ValidationException("Stock amount must be positive");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.signum() <= 0) {
            throw new ValidationException("Price must be positive");
        }
    }
}