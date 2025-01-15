ALTER TABLE receipt
    ADD CONSTRAINT fk_receipt_customer
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id);

ALTER TABLE receipt
    ADD CONSTRAINT fk_receipt_order
    FOREIGN KEY (order_id) REFERENCES "order" (id);

ALTER TABLE receipt
     ADD CONSTRAINT fk_receipt_payment
     FOREIGN KEY (transaction_id) REFERENCES payment (transaction_id);

CREATE INDEX IF NOT EXISTS payment_amount_idx
    ON payment(amount);

CREATE INDEX IF NOT EXISTS receipt_status_idx
    ON receipt(status);
