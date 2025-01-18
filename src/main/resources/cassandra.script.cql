CREATE TABLE test_keyspace.denormalized_receipt (
    id TEXT PRIMARY KEY,
    date TIMESTAMP,
    status TEXT,
    transaction_id TEXT,
    order_id TEXT,
    customer_id TEXT,
    tracking_number TEXT,

    method TEXT,
    amount DOUBLE,
    currency TEXT,
    card_type TEXT,
    card_last_four_digits TEXT,
    card_expiry_date TEXT,

    item TEXT, -- Use JSON strings for complex data types
    shipping_address TEXT, -- Use JSON strings for complex data types

    name TEXT,
    email TEXT,
    phone TEXT,
    loyalty_points_earned INT,
    loyalty_points_balance INT
);

// Cassandra does not support traditional indexes well. Avoid creating unnecessary indexes if possible
// Alternatively, consider secondary indexing only when absolutely necessary.
CREATE INDEX IF NOT EXISTS ON test_keyspace.denormalized_receipt (customer_id);