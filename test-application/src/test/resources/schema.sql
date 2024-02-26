DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS replicated_persons;

CREATE TABLE persons
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(256) NOT NULL,
    surname VARCHAR(256) NOT NULL,
    patronymic VARCHAR(256) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE replicated_persons(
    id INTEGER PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    surname VARCHAR(256) NOT NULL,
    birth_date DATE NOT NULL
);
