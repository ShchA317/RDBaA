CREATE TABLE denormalized_receipt (
    id VARCHAR(255) PRIMARY KEY,
    date TIMESTAMP,
    status VARCHAR(255),
    transaction_id VARCHAR(255),
    order_id VARCHAR(255),
    customer_id VARCHAR(255),
    tracking_number VARCHAR(255),

    method VARCHAR(255),
    amount FLOAT,
    currency VARCHAR(255),
    card_type VARCHAR(255),
    card_last_four_digits VARCHAR(255),
    card_expiry_date VARCHAR(255),

    item JSONB,
    shipping_address JSONB,

    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    loyalty_points_earned integer,
    loyalty_points_balance integer
);


CREATE INDEX IF NOT EXISTS denormalized_receipt_customer_id
    ON denormalized_receipt(customer_id);
