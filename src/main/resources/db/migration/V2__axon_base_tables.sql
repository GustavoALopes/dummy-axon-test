CREATE SEQUENCE IF NOT EXISTS domain_event_entry_seq START 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS domain_event_entry (
    global_index BIGINT PRIMARY KEY DEFAULT nextval('domain_event_entry_seq'), -- Unique index for ordering events
    aggregate_identifier VARCHAR(255) NOT NULL, -- Event's unique identifier
    event_identifier VARCHAR(255) NOT NULL,  -- Event's unique identifier
    meta_data BIGINT, -- Serialized metadata
    payload BIGINT, -- Serialized event payload
    payload_revision VARCHAR(255),
    payload_type VARCHAR(255), -- Fully-qualified name of the payload class
    sequence_number BIGINT NOT NULL, -- Order of events within the aggregate
    time_stamp VARCHAR(255) NOT NULL, -- Timestamp of the event
    type VARCHAR(255) NOT NULL -- Aggregate type
);

CREATE UNIQUE INDEX IF NOT EXISTS domain_event_entry_index ON domain_event_entry (aggregate_identifier, sequence_number);
CREATE INDEX IF NOT EXISTS domain_event_entry_type_index ON domain_event_entry (type);

CREATE TABLE IF NOT EXISTS snapshot_event_entry (
    event_identifier VARCHAR(255) NOT NULL,      -- Snapshot's unique identifier
    aggregate_identifier VARCHAR(255) NOT NULL,  -- Identifier for the aggregate
    sequence_number BIGINT NOT NULL,             -- Order of the snapshot in the aggregate's lifecycle
    type VARCHAR(255) NOT NULL,                  -- Aggregate type
    timestamp VARCHAR(255) NOT NULL,             -- Timestamp of the snapshot
    payload BYTEA NOT NULL,                      -- Serialized snapshot payload
    payload_type VARCHAR(255) NOT NULL,          -- Fully-qualified name of the payload class
    metadata BYTEA,                              -- Serialized metadata
    PRIMARY KEY (aggregate_identifier, sequence_number)
);

CREATE TABLE IF NOT EXISTS token_entry (
    processor_name VARCHAR(255) NOT NULL,        -- Name of the event processor
    segment INT NOT NULL,                        -- Segment ID for the processor
    token BYTEA,                                 -- Serialized token
    token_type VARCHAR(255),                     -- Token's type
    timestamp VARCHAR(255) NOT NULL,             -- Last updated timestamp
    owner VARCHAR(255),                          -- Owner of the token
    PRIMARY KEY (processor_name, segment)
);

CREATE SEQUENCE IF NOT EXISTS association_value_entry_seq START 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS association_value_entry (
    id BIGSERIAL PRIMARY KEY,                    -- Unique identifier
    association_key VARCHAR(255) NOT NULL,       -- Association key
    association_value VARCHAR(255) NOT NULL,     -- Association value
    saga_id VARCHAR(255) NOT NULL,               -- Saga identifier
    saga_type VARCHAR(255) NOT NULL              -- Saga type
);
CREATE INDEX IF NOT EXISTS association_value_entry_index ON association_value_entry (association_key, association_value, saga_type);

CREATE TABLE IF NOT EXISTS saga_entry (
    saga_id VARCHAR(255) NOT NULL,               -- Saga identifier
    revision VARCHAR(255),                       -- Revision of the serialized saga
    saga_type VARCHAR(255) NOT NULL,             -- Saga type
    serialized_saga BYTEA NOT NULL,              -- Serialized saga instance
    PRIMARY KEY (saga_id, saga_type)
);

-- Dead Letter Queue Table
CREATE TABLE IF NOT EXISTS dead_letter_entry (
    dead_letter_id VARCHAR(255) PRIMARY KEY, -- Unique identifier for each dead letter
    processing_group VARCHAR(255) NOT NULL,
    aggregate_identifier VARCHAR(255) NOT NULL,
    sequence_number BIGINT NOT NULL,
    sequence_identifier VARCHAR(255) NOT NULL,
    sequence_index BIGINT NOT NULL,
    event_identifier VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    token_type VARCHAR(255),
    type VARCHAR(255),
    dead_letter_type VARCHAR(255),
    message_id VARCHAR(255),
    message_type VARCHAR(255) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    payload_type VARCHAR(255) NOT NULL,
    payload_revision VARCHAR(255),
    payload BIGINT NOT NULL,
    meta_data BIGINT,
    diagnostics BIGINT,
    cause_type VARCHAR(255),
    cause_message VARCHAR(255),
    enqueued_at TIMESTAMP NOT NULL,
    last_touched_at TIMESTAMP,
    last_touched TIMESTAMP NOT NULL,
    processing_started TIMESTAMP,
    UNIQUE (processing_group, sequence_number)
);

-- Optional: Create index for faster querying by processing group
CREATE INDEX IF NOT EXISTS idx_dead_letter_processing_group
ON dead_letter_entry (processing_group);



