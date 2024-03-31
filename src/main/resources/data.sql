-- Вставка данных в таблицу MPA с использованием оператора MERGE
MERGE INTO MPA AS target
    USING (VALUES (1, 'G'),
                  (2, 'PG'),
                  (3, 'PG-13'),
                  (4, 'R'),
                  (5, 'NC-17')) AS source (id, name)
    ON target.id = source.id
    WHEN NOT MATCHED THEN
        INSERT (id, name)
            VALUES (source.id, source.name);

-- Вставка данных в таблицу Genre с использованием оператора MERGE
MERGE INTO Genre AS target
    USING (VALUES (1, 'Комедия'),
                  (2, 'Драма'),
                  (3, 'Мультфильм'),
                  (4, 'Триллер'),
                  (5, 'Документальный'),
                  (6, 'Боевик')) AS source (id, name)
    ON target.id = source.id
    WHEN NOT MATCHED THEN
        INSERT (id, name)
            VALUES (source.id, source.name);
