package com.supermarket.catalog.repository;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindProduct() {
        Product product = new Product(
                UUID.randomUUID(),
                "Milk",
                Category.DAIRY,
                BigDecimal.valueOf(5.90),
                0,
                "Local Supplier",
                "Fresh milk",
                Instant.now()
        );

        productRepository.save(product);

        var found = productRepository.findById(product.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Milk");
    }
}