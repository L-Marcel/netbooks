CREATE TABLE IF NOT EXISTS Plans(
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(400) NOT NULL,
    duration BIGINT DEFAULT 0
);