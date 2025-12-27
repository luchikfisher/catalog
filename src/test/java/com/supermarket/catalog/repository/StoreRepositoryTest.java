package com.supermarket.catalog.repository;

import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.testinfra.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StoreRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void saveAndFindStoreByName() {

        UUID storeId = UUID.randomUUID();
        Instant insertionTime = Instant.parse("2024-01-01T00:00:00Z");

        Store store = Store.builder()
                .id(storeId)
                .name("Central")
                .insertionTime(insertionTime)
                .build();

        storeRepository.save(store);

        Optional<Store> found = storeRepository.findByName("Central");

        assertThat(found)
                .isPresent()
                .get()
                .satisfies(s -> {
                    assertThat(s.getId()).isEqualTo(storeId);
                    assertThat(s.getName()).isEqualTo("Central");
                    assertThat(s.getInsertionTime()).isEqualTo(insertionTime);
                });
    }
}
