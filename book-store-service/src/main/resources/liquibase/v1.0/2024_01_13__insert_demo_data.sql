--liquibase formatted sql

--changeset VolginTA: test_data context:prod
INSERT INTO authors(id, fio, birthday)
VALUES (1, 'Александр Сергеевич Пушкин', '1799-06-06'),
       (2, 'Михаил Юрьевич Лермонтов', '1814-10-15'),
       (3, 'Толстой Алексей Константинович', '1817-09-05');
ALTER TABLE authors ALTER COLUMN id RESTART WITH 4;

INSERT INTO genres(id, name)
VALUES (1, 'Поэма'),
       (2, 'Повесть'),
       (3, 'Роман'),
       (4, 'Баллада'),
       (5, 'Трагедия');
ALTER TABLE genres ALTER COLUMN id RESTART WITH 6;

INSERT INTO books(id, title, published, number_of, genre_id, author_id)
VALUES (1, 'Руслан и Людмила', '1820-04-01', 5, 1, 1),
       (2, 'Пиковая дама', '1834-04-01', 3, 2, 1),
       (3, 'Евгений Онегин', '1831-10-05', 0, 3, 1),
       (4, 'Бородино', '1812-09-07', 5, 4, 2),
       (5, 'Герой нашего времени', '1839-03-01', 6, 3, 2),
       (6, 'Царь Борис', '1870-01-01', 1, 5, 3),
       (7, 'Смерть Иоанна Грозного', '1865-01-01', 0, 5, 3);
ALTER TABLE books ALTER COLUMN id RESTART WITH 8;

INSERT INTO products(id, description, price, disabled)
VALUES (1, 'Книга Руслан и Людмила', 350.00, false),
       (2, 'Книга Пиковая дама', 200.00, false),
       (3, 'Книга Евгений Онегин', 300.00, false),
       (4, 'Книга Бородино', 150.00, true),
       (5, 'Книга Герой нашего времени', 200.00, false),
       (6, 'Книга Царь Борис', 200.00, false),
       (7, 'Книга Смерть Иоанна Грозного', 200.00, false),
       (8, 'Избранные сочинения Пушкина', 1000.00, false),
       (9, 'Избранные сочинения Толстого', 700.00, false),
       (10, 'Избранные сочинения Лермонтова', 500.00, false);
ALTER TABLE products ALTER COLUMN id RESTART WITH 11;

INSERT INTO products_books(product_id, book_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 1),
       (8, 2),
       (8, 3),
       (9, 6),
       (9, 7),
       (10, 4),
       (10, 5);
