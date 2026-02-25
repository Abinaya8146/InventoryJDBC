CREATE DATABASE inventorydb;

USE inventorydb;

CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

INSERT INTO products (name, quantity, price)
VALUES ('Laptop', 10, 55000);