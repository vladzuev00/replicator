DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS replicated_persons;

CREATE TABLE addresses
(
    id      SERIAL PRIMARY KEY,
    country VARCHAR(256) NOT NULL,
    city    VARCHAR(256) NOT NULL
);

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

CREATE TABLE replicated_addresses
(
    id      SERIAL PRIMARY KEY,
    country VARCHAR(256) NOT NULL,
    city    VARCHAR(256) NOT NULL
);

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


