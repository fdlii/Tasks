CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    money NUMERIC
);

CREATE TABLE IF NOT EXISTS transfers (
    id SERIAL PRIMARY KEY,
    withdrawal_account_id INT,
    transfer_account_id INT,
    summ NUMERIC,
    status INT,

    CONSTRAINT fk_withdrawal
    FOREIGN KEY (withdrawal_account_id)
    REFERENCES accounts(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);