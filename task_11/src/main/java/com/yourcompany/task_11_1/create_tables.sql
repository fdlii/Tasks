CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    clientID BIGINT NOT NULL,
    discount NUMERIC NOT NULL,
    finalPrice NUMERIC(12,2) NOT NULL,
    executionDate TIMESTAMP NOT NULL,
    orderStatus INT NOT NULL,

    CONSTRAINT fk_client
        FOREIGN KEY (clientID)
        REFERENCES clients(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(50) NOT NULL,
    description TEXT,
    published TIMESTAMP NOT NULL,
    inStock BOOLEAN NOT NULL,
    countInStock INT NOT NULL,
    price NUMERIC(12,2) NOT NULL
);

CREATE TABLE orders_books (
    bookID BIGINT,
    orderID BIGINT,

    CONSTRAINT fk_orders
        FOREIGN KEY (orderID)
        REFERENCES orders(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_books
        FOREIGN KEY (bookID)
        REFERENCES books(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE requests (
    id BIGSERIAL PRIMARY KEY,
    bookID BIGINT NOT NULL,
    count INT NOT NULL,
    isOpen BOOLEAN NOT NULL,

    CONSTRAINT fk_requests
        FOREIGN KEY (bookID)
        REFERENCES books(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);