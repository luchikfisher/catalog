package com.supermarket.catalog.test_config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class PostgresTestContainer {

    private static final DockerImageName POSTGRES_IMAGE =
            DockerImageName.parse("postgres:15")
                    .asCompatibleSubstituteFor("postgres");

    public static PostgreSQLContainer<?> getInstance() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName("test_db")
                    .withUsername("test_user")
                    .withPassword("test_pass")
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofSeconds(60))
                    .withImagePullPolicy(imageName -> false)
                    .withExposedPorts(5432);

        container.start();
        return container;
    }
}