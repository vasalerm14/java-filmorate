-- Вставка данных в таблицу MPA
INSERT INTO MPA (id, name)
SELECT id, name
FROM (VALUES (1, 'G'),
             (2, 'PG'),
             (3, 'PG-13'),
             (4, 'R'),
             (5, 'NC-17')) AS new_values (id, name)
WHERE NOT EXISTS (SELECT 1
                  FROM MPA
                  WHERE MPA.id = new_values.id);

-- Вставка данных в таблицу Genre
INSERT INTO Genre (id, name)
SELECT id, name
FROM (VALUES (1, 'Комедия'),
             (2, 'Драма'),
             (3, 'Мультфильм'),
             (4, 'Триллер'),
             (5, 'Документальный'),
             (6, 'Боевик')) AS new_values (id, name)
WHERE NOT EXISTS (SELECT 1
                  FROM Genre
                  WHERE Genre.id = new_values.id);

