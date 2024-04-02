INSERT INTO addresses(id, country, city) VALUES(255, 'Russia', 'Moscow');
INSERT INTO addresses(id, country, city) VALUES(256, 'America', 'Chicago');
INSERT INTO addresses(id, country, city) VALUES(257, 'Austria', 'Vienna');
INSERT INTO addresses(id, country, city) VALUES(258, 'Austria', 'Styria');
INSERT INTO addresses(id, country, city) VALUES(259, 'Austria', 'Tyrol');
INSERT INTO addresses(id, country, city) VALUES(260, 'Estonia', 'Tallinn');
INSERT INTO addresses(id, country, city) VALUES(261, 'Estonia', 'Tartu');
INSERT INTO addresses(id, country, city) VALUES(262, 'Estonia', 'Narva');
INSERT INTO addresses(id, country, city) VALUES(263, 'Armenia', 'Yerevan');

INSERT INTO replicated_addresses(id, country, city) VALUES(255, 'Russia', 'Moscow');
INSERT INTO replicated_addresses(id, country, city) VALUES(256, 'America', 'Chicago');
INSERT INTO replicated_addresses(id, country, city) VALUES(257, 'Austria', 'Vienna');
INSERT INTO replicated_addresses(id, country, city) VALUES(258, 'Austria', 'Styria');
INSERT INTO replicated_addresses(id, country, city) VALUES(259, 'Austria', 'Tyrol');
INSERT INTO replicated_addresses(id, country, city) VALUES(260, 'Estonia', 'Tallinn');
INSERT INTO replicated_addresses(id, country, city) VALUES(261, 'Estonia', 'Tartu');
INSERT INTO replicated_addresses(id, country, city) VALUES(262, 'Estonia', 'Narva');

INSERT INTO persons(id, name, surname, patronymic, birth_date, address_id) VALUES(255, 'Vlad', 'Zuev', 'Sergeevich', '2000-02-18', 255);
INSERT INTO persons(id, name, surname, patronymic, birth_date, address_id) VALUES(256, 'Vasilii', 'Dolzhikov', 'Borisovich', '1980-03-15', 255);
INSERT INTO persons(id, name, surname, patronymic, birth_date, address_id) VALUES(257, 'Fedya', 'Amelin', 'Yemelyanovich', '1990-04-15', 255);
INSERT INTO persons(id, name, surname, patronymic, birth_date, address_id) VALUES(258, 'Pashenka', 'Kornev', 'Filippovich', '1995-04-23', 256);
INSERT INTO persons(id, name, surname, patronymic, birth_date, address_id) VALUES(259, 'Sasha', 'Kuzmin', 'Gennadiyevich', '1996-02-21', 257);

INSERT INTO replicated_persons(id, name, surname, birth_date, address_id) VALUES(255, 'Vlad', 'Zuev', '2000-02-18', 255);
INSERT INTO replicated_persons(id, name, surname, birth_date, address_id) VALUES(256, 'Vasilii', 'Dolzhikov', '1980-03-15', 255);
INSERT INTO replicated_persons(id, name, surname, birth_date, address_id) VALUES(257, 'Fedya', 'Amelin', '1990-04-15', 255);
INSERT INTO replicated_persons(id, name, surname, birth_date, address_id) VALUES(258, 'Pashenka', 'Kornev', '1995-04-23', 256);
INSERT INTO replicated_persons(id, name, surname, birth_date, address_id) VALUES(259, 'Sasha', 'Gennadiyevich', '1996-02-21', 257);
