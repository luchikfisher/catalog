package com.supermarket.catalog.repository;

import com.supermarket.catalog.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindUserById() {
        User user = new User(
                UUID.randomUUID(),
                "john_doe",
                "secret",
                "john@example.com",
                Instant.now()
        );

        userRepository.save(user);

        var found = userRepository.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john_doe");
    }

    @Test
    void existsByUsername_returnsTrueWhenUserExists() {
        User user = new User(
                UUID.randomUUID(),
                "unique_user",
                "password",
                "user@example.com",
                Instant.now()
        );

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("unique_user");

        assertThat(exists).isTrue();
    }
}