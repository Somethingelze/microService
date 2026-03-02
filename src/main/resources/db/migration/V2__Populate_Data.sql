INSERT INTO users (id, username, password, role)
VALUES
    (gen_random_uuid(), 'david.coperfield@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'ADMIN'),
    (gen_random_uuid(), 'john.smith@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    (gen_random_uuid(), 'jane.doe@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    (gen_random_uuid(), 'michael.brown@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    (gen_random_uuid(), 'emily.johnson@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'USER'),
    (gen_random_uuid(), 'david.wilson@gmail.com', '$2b$12$2jBqIQ0qkO6LOPB1RHbYLe1JLhXXSHSGUxQcfkiw.HKZ9YpJWi4Ti', 'ADMIN');

DO $$
    DECLARE
        u RECORD;
        num_orders INT;
        i INT;
    BEGIN
        FOR u IN SELECT id, username FROM users LOOP
                num_orders := floor(random() * 5 + 1)::int;
                FOR i IN 1..num_orders LOOP
                        INSERT INTO orders (user_id, description, status, created_at)
                        VALUES (u.id, concat('Order ', i, ' for ', u.username), 'CREATED', NOW());
                    END LOOP;
            END LOOP;
    END $$;