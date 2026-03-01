-- 1. Сначала вставляем пользователей (ID сгенерируется автоматически)
INSERT INTO users (username, password, role)
VALUES
    ('some@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'ADMIN'),
    ('john.smith@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    ('jane.doe@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    ('michael.brown@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    ('emily.johnson@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    ('david.wilson@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'ADMIN');

-- 2. Теперь генерируем заказы для созданных пользователей
DO $$
    DECLARE
        u RECORD;
        num_orders INT;
        i INT;
    BEGIN
        FOR u IN SELECT id, username FROM users LOOP
                -- случайное количество заказов от 1 до 5
                num_orders := floor(random() * 5 + 1)::int;
                FOR i IN 1..num_orders LOOP
                        INSERT INTO orders (user_id, description, status, created_at)
                        VALUES (u.id, concat('Order ', i, ' for ', u.username), 'CREATED', NOW());
                    END LOOP;
            END LOOP;
    END $$;