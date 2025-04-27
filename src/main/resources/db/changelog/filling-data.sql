INSERT INTO "user" (name, date_of_birth, password)
VALUES ('John Doe', '1990-01-01', '$2a$10$xJwL5v5zLt2X5ZQ1JQYJQe5vQ5ZQ1JQYJQe5vQ5ZQ1JQYJQe5vQ5ZQ1JQYJQ'),
       ('Jane Smith', '1985-05-15', '$2a$10$xJwL5v5zLt2X5ZQ1JQYJQe5vQ5ZQ1JQYJQe5vQ5ZQ1JQYJQe5vQ5ZQ1JQYJQ');

INSERT INTO account (user_id, balance, initial_deposit)
VALUES (1, 100.00, 100.00),
       (2, 200.00, 200.00);

INSERT INTO email_data (user_id, email)
VALUES (1, 'john.doe@example.com'),
       (2, 'jane.smith@example.com');

INSERT INTO phone_data (user_id, phone)
VALUES (1, '79201234567'),
       (2, '79207654321');