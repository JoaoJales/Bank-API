create table transactions(
    id BIGINT NOT NULL AUTO_INCREMENT,
    origin_account_id BIGINT,
    destiny_account_id BIGINT,
    valor DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    data datetime not null,
    tipo varchar(100),
    descricao VARCHAR(50),

    PRIMARY KEY (id),
    CONSTRAINT fk_transactions_origin FOREIGN KEY (origin_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transactions_destiny FOREIGN KEY (destiny_account_id) REFERENCES accounts(id)
);