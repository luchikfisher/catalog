package com.supermarket.catalog.testconfig;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(BaseIntegrationTest.FixedClockTestConfig.class)
public abstract class BaseIntegrationTest
        extends BasePostgresTest {

    public static final Instant FIXED_INSTANT =
            Instant.parse("2024-01-01T00:00:00Z");

    @TestConfiguration
    static class FixedClockTestConfig {

        @Bean
        public Clock fixedClock() {
            return Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);
        }
    }
}