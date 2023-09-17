DROP DATABASE IF EXISTS lavender_company ;

CREATE DATABASE lavender_company;

USE lavender_company;

CREATE TABLE customer(
	id VARCHAR(50),
    first_name VARCHAR(50),
    last_name NVARCHAR(50),
    address NVARCHAR(100),
    age SMALLINT,
    status VARCHAR(50)
);

CREATE TABLE contract (
	id VARCHAR(50),
    customer_id VARCHAR(50),
    apartment_id VARCHAR(50),
    start_date DATE,
    end_date DATE
);

CREATE TABLE apartment (
	id VARCHAR(50),
    address NVARCHAR(100),
    rental_price VARCHAR(12),
    number_of_rooms SMALLINT
);

-- ADD CONSTRAINTS --

-- PRIMARY KEY CONSTRAINTS --
ALTER TABLE customer ADD CONSTRAINT customer_pk PRIMARY KEY (id);
ALTER TABLE contract ADD CONSTRAINT contract_pk PRIMARY KEY (id);
ALTER TABLE apartment ADD CONSTRAINT apartment_pk PRIMARY KEY (id);

-- FOREIGN KEY CONSTRAINTS --
ALTER TABLE contract ADD CONSTRAINT contract_user_fk FOREIGN KEY (customer_id) REFERENCES customer(id);
ALTER TABLE contract ADD CONSTRAINT contract_apartment_fk FOREIGN KEY (apartment_id) REFERENCES apartment(id);

-- OTHER CONSTRAINTS --
ALTER TABLE customer ADD CONSTRAINT customer_age CHECK (age > 0);
ALTER TABLE apartment ADD CONSTRAINT apartment_no_of_rooms CHECK (number_of_rooms > 0);
ALTER TABLE contract ADD CONSTRAINT contract_start_and_end_date CHECK (start_date < end_date);







