
CREATE DATABASE  food_ordering_db;
USE food_ordering_db;

CREATE TABLE food_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    price DOUBLE
);

CREATE TABLE delivery_agents (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100)
);

CREATE TABLE discounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    min_amount DOUBLE,
    discount_amount DOUBLE
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100),
    items TEXT,
    total DOUBLE,
    discount DOUBLE,
    payment_mode VARCHAR(20),
    delivery_partner VARCHAR(50),
    final_amount DOUBLE
);
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role ENUM('admin', 'customer')
);

INSERT INTO users (username, password, role) VALUES 
('admin1', 'admin123', 'admin'),
('rucha', '1234', 'customer');
INSERT INTO discounts (min_amount, discount_amount) VALUES (500, 50);
INSERT INTO delivery_agents (name) VALUES ('Zomato'), ('Swiggy');
