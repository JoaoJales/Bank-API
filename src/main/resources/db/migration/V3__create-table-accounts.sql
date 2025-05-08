CREATE TABLE accounts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero VARCHAR(20) UNIQUE NOT NULL,
    saldo DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    customer_id BIGINT NOT NULL,
    ativo TINYINT NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    CONSTRAINT fk_accounts_customer_id FOREIGN KEY (customer_id) REFERENCES customers(id)
);