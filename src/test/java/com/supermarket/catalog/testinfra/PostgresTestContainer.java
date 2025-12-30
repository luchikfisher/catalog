package com.supermarket.catalog.testinfra;

import lombok.NoArgsConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.ContainerLaunchException;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@NoArgsConstructor
public final class PostgresTestContainer {

    private static final DockerImageName POSTGRES_IMAGE =
            DockerImageName.parse("postgres:15")
                    .asCompatibleSubstituteFor("postgres");

    private static final int POSTGRES_PORT = 15432;

    private static PostgreSQLContainer<?> container;

    public static synchronized PostgreSQLContainer<?> getInstance() {
        if (container == null) {
            container = new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName("test_db")
                    .withUsername("test_user")
                    .withPassword("test_pass")
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofSeconds(60))
                    .withImagePullPolicy(imageName -> false)
                    .withExposedPorts(5432)
                    .withCreateContainerCmdModifier(cmd ->
                            cmd.getHostConfig().withPortBindings(
                                    new com.github.dockerjava.api.model.PortBinding(
                                            com.github.dockerjava.api.model.Ports.Binding.bindPort(POSTGRES_PORT),
                                            new com.github.dockerjava.api.model.ExposedPort(5432)
                                    )
                            )
                    )
                    .withLogConsumer(new Slf4jLogConsumer(
                            LoggerFactory.getLogger("POSTGRES-TEST")));

            try {
                container.start();
            } catch (Exception e) {
                throw new ContainerLaunchException(
                        "Failed to start PostgreSQL Testcontainer. " +
                                "Ensure Docker is running and postgres:15 image exists locally.",
                        e
                );
            }
        }
        return container;
    }
}