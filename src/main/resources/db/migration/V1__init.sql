-- sanity migration
CREATE TABLE IF NOT EXISTS flyway_test (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL
);