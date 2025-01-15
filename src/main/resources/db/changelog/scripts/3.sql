ALTER TABLE receipt
    ADD CONSTRAINT fk_receipt_customer
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id);

CREATE INDEX IF NOT EXISTS receipt_customer_id_idx
    ON receipt(customer_id);

ALTER TABLE receipt
    ADD CONSTRAINT fk_receipt_order
    FOREIGN KEY (order_id) REFERENCES "order" (id);

CREATE INDEX IF NOT EXISTS receipt_order_id_idx
    ON receipt(order_id);

ALTER TABLE receipt
     ADD CONSTRAINT fk_receipt_payment
     FOREIGN KEY (transaction_id) REFERENCES payment (transaction_id);

CREATE INDEX IF NOT EXISTS receipt_transaction_id_idx
    ON receipt(transaction_id);


DROP INDEX IF EXISTS payment_amount_idx;
CREATE INDEX IF NOT EXISTS payment_amount_idx
    ON payment(amount);


DROP INDEX IF EXISTS receipt_status_idx;
CREATE INDEX IF NOT EXISTS receipt_status_idx
    ON receipt(status);
