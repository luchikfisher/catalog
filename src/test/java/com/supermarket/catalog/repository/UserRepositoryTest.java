package com.supermarket.catalog.repository;

import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.testinfra.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void saveAndFindUserById() {

        UUID userId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Instant insertionTime = Instant.parse("2024-01-01T00:00:00Z");

        Store store = Store.builder()
                .id(storeId)
                .name("Downtown")
                .insertionTime(insertionTime)
                .build();

        storeRepository.save(store);

        User user = User.builder()
                .id(userId)
                .username("john_doe")
                .password("secret")
                .email("john@example.com")
                .store(store)
                .insertionTime(insertionTime)
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findById(user.getId());

        assertThat(found)
                .isPresent()
                .get()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(userId);
                    assertThat(u.getUsername()).isEqualTo("john_doe");
                    assertThat(u.getPassword()).isEqualTo("secret");
                    assertThat(u.getEmail()).isEqualTo("john@example.com");
                    assertThat(u.getStore().getId()).isEqualTo(storeId);
                    assertThat(u.getInsertionTime()).isEqualTo(insertionTime);
                });
    }

    @Test
    void existsByUsername_returnsTrueWhenExists() {
        // given
        UUID userId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Instant insertionTime = Instant.parse("2024-01-01T00:00:00Z");

        Store store = Store.builder()
                .id(storeId)
                .name("Central")
                .insertionTime(insertionTime)
                .build();

        storeRepository.save(store);

        User user = User.builder()
                .id(userId)
                .username("unique_user")
                .password("password")
                .email("user@example.com")
                .store(store)
                .insertionTime(insertionTime)
                .build();

        // when
        userRepository.save(user);

        // then
        assertThat(userRepository.existsByUsername("unique_user")).isTrue();
    }
}
