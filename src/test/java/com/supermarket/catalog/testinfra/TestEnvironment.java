package com.supermarket.catalog.testinfra;

import org.springframework.test.context.DynamicPropertyRegistry;

public final class TestEnvironment {

    private TestEnvironment() {
    }

    public static void registerPostgres(DynamicPropertyRegistry registry) {

        var postgres = PostgresTestContainer.getInstance();

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "false");
    }
}