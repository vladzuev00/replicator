DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS replicated_persons;
DROP TABLE IF EXISTS replicated_addresses;

CREATE TABLE addresses
(
    id      SERIAL PRIMARY KEY,
    country VARCHAR(256) NOT NULL,
    city    VARCHAR(256) NOT NULL
);

ALTER TABLE addresses
    ADD CONSTRAINT address_names_should_be_unique
        UNIQUE (country, city);

CREATE TABLE persons
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(256) NOT NULL,
    surname    VARCHAR(256) NOT NULL,
    patronymic VARCHAR(256) NOT NULL,
    birth_date DATE         NOT NULL,
    address_id INTEGER      NOT NULL
);

ALTER TABLE persons
    ADD CONSTRAINT fk_persons_to_addresses
        FOREIGN KEY (address_id)
            REFERENCES addresses (id);

CREATE TABLE phones
(
    id        SERIAL PRIMARY KEY,
    number    VARCHAR(9) NOT NULL,
    person_id INTEGER    NOT NULL
);

ALTER TABLE persons
    ADD CONSTRAINT fk_phones_to_persons
        FOREIGN KEY (person_id)
            REFERENCES persons (id);

CREATE TABLE replicated_addresses
(
    id      SERIAL PRIMARY KEY,
    country VARCHAR(256) NOT NULL,
    city    VARCHAR(256) NOT NULL
);

ALTER TABLE replicated_addresses
    ADD CONSTRAINT replicated_address_names_should_be_unique
        UNIQUE (country, city);

CREATE TABLE replicated_persons
(
    id         INTEGER PRIMARY KEY,
    name       VARCHAR(256) NOT NULL,
    surname    VARCHAR(256) NOT NULL,
    birth_date DATE         NOT NULL,
    address_id INTEGER      NOT NULL
);

ALTER TABLE replicated_persons
    ADD CONSTRAINT fk_replicated_persons_to_replicated_addresses
        FOREIGN KEY (address_id)
            REFERENCES replicated_addresses (id);


