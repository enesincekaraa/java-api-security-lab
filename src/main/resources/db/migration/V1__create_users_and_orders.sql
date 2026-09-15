CREATE TABLE lab_users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    role     VARCHAR(30)  NOT NULL,

    CONSTRAINT chk_lab_users_role
        CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE customer_orders
(
    id           UUID PRIMARY KEY,
    owner_id     UUID           NOT NULL,
    product_name VARCHAR(200)   NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    currency     CHAR(3)        NOT NULL,
    created_at   TIMESTAMPTZ    NOT NULL,

    CONSTRAINT fk_customer_orders_owner
        FOREIGN KEY (owner_id)
            REFERENCES lab_users (id),

    CONSTRAINT chk_customer_orders_product_name
        CHECK (BTRIM(product_name) <> ''),

    CONSTRAINT chk_customer_orders_total_amount
        CHECK (total_amount > 0),

    CONSTRAINT chk_customer_orders_currency
        CHECK (currency = UPPER(currency))
);

CREATE INDEX idx_customer_orders_owner_id
    ON customer_orders (owner_id);