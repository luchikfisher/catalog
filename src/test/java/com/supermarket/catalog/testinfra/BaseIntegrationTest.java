package com.supermarket.catalog.testinfra;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @BeforeAll
    static void startContainer() {
        PostgresTestContainer.getInstance();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        TestEnvironment.registerPostgres(registry);
    }
}