drop table receipt;

CREATE TABLE receipt (
    id VARCHAR(255) PRIMARY KEY,
    date TIMESTAMP,
    status VARCHAR(255),
    transaction_id VARCHAR(255),
    order_id VARCHAR(255),
    customer_id VARCHAR(255),
    tracking_number VARCHAR(255)
--    FOREIGN KEY (transaction_id) REFERENCES payment(transaction_id),
--    FOREIGN KEY (order_id) REFERENCES "order"(id),
--    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE payment (
    transaction_id VARCHAR(255) PRIMARY KEY,
    method VARCHAR(255),
    amount FLOAT,
    currency VARCHAR(255),
    card_type VARCHAR(255),
    card_last_four_digits VARCHAR(255),
    card_expiry_date VARCHAR(255)
);

CREATE TABLE "order" (
    id VARCHAR(255) PRIMARY KEY,
    item JSONB,
    shipping_address JSONB
);

CREATE TABLE customer (
    customer_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    loyalty_points_earned integer,
    loyalty_points_balance integer
);
