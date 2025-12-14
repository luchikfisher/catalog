package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void cannotDecreaseStockBelowZero() {

        UUID productId = productService.createProduct(
                "Bread",
                Category.BAKERY,
                BigDecimal.valueOf(4.5),
                "Local Bakery",
                "Fresh bread"
        );

        assertThatThrownBy(() ->
                productService.decreaseStock(productId, 1)
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void cannotCreateProductWithNegativePrice() {

        assertThatThrownBy(() ->
                productService.createProduct(
                        "Invalid",
                        Category.DAIRY,
                        BigDecimal.valueOf(-1),
                        "Supplier",
                        null
                )
        ).isInstanceOf(ValidationException.class);
    }
}