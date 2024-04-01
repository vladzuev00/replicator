INSERT INTO addresses(id, country, city) VALUES(255, 'Belarus', 'Minsk');
INSERT INTO addresses(id, country, city) VALUES(256, 'America', 'Chicago');

INSERT INTO replicated_addresses(id, country, city) VALUES(255, 'Belarus', 'Minsk');
INSERT INTO replicated_addresses(id, country, city) VALUES(256, 'America', 'Chicago');

INSERT INTO persons(id, name, surname, patronymic, birth_date, address_id) VALUES(255, 'Vlad', 'Zuev', 'Sergeevich', '2000-02-18', 255);

INSERT INTO replicated_persons(id, name, surname, birth_date, address_id) VALUES(255, 'Vlad', 'Zuev', '2000-02-18', 255);
