-- Create bike table
CREATE TABLE IF NOT EXISTS bikes (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create user table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    document VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create unique index on document field
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_document ON users (document);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users (email);
