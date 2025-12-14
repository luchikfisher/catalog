package com.supermarket.catalog.domain.product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private String supplier;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Product() {
        // for JPA
    }

    public Product(
            UUID id,
            String name,
            Category category,
            BigDecimal price,
            int stockQuantity,
            String supplier,
            String description,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.supplier = supplier;
        this.description = description;
        this.createdAt = createdAt;
    }

    // getters only
}