
CREATE TABLE t_order_events (
                                id VARCHAR(36) NOT NULL,
                                aggregatetype VARCHAR(255) NOT NULL,
                                aggregateid VARCHAR(255) NOT NULL,
                                type VARCHAR(255) NOT NULL,
                                payload JSON NOT NULL,
                                created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

                                CONSTRAINT pk_t_order_events PRIMARY KEY (id),

                                INDEX idx_order_events_aggregate (
        aggregatetype,
        aggregateid,
        created_at
    )
);
