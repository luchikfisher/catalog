package com.supermarket.catalog.repository;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.testinfra.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void saveAndFindProduct() {

        UUID productId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Instant insertionTime = Instant.parse("2024-01-01T00:00:00Z");

        Store store = Store.builder()
                .id(storeId)
                .name("Uptown")
                .insertionTime(insertionTime)
                .build();

        storeRepository.save(store);

        Product product = Product.builder()
                .id(productId)
                .name("Milk")
                .category(Category.DAIRY)
                .price(BigDecimal.valueOf(5.90))
                .stockQuantity(0)
                .supplier("Local Supplier")
                .description("Fresh milk")
                .store(store)
                .insertionTime(insertionTime)
                .build();

        productRepository.save(product);

        Optional<Product> found = productRepository.findById(product.getId());

        assertThat(found)
                .isPresent()
                .get()
                .satisfies(p -> {
                    assertThat(p.getId()).isEqualTo(productId);
                    assertThat(p.getName()).isEqualTo("Milk");
                    assertThat(p.getCategory()).isEqualTo(Category.DAIRY);
                    assertThat(p.getPrice()).isEqualByComparingTo("5.90");
                    assertThat(p.getStockQuantity()).isZero();
                    assertThat(p.getSupplier()).isEqualTo("Local Supplier");
                    assertThat(p.getDescription()).isEqualTo("Fresh milk");
                    assertThat(p.getStore().getId()).isEqualTo(storeId);
                    assertThat(p.getInsertionTime()).isEqualTo(insertionTime);
                });
    }
}
