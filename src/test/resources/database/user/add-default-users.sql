INSERT INTO users (id, email, first_name, last_name, password, is_deleted) VALUES
    (1, 'john.example@email.com', 'John', 'Doe', '$2a$10$yvofprfFFpxZatoflOuduupLzGqFWizREUJ0.lcQMDREJ28r8XB6.', false),
    (2, 'jane.example@gmail.com', 'Jane', 'Doe', '$2a$10$yvofprfFFpxZatoflOuduupLzGqFWizREUJ0.lcQMDREJ28r8XB6.', false);

INSERT INTO users_roles (user_id, role_id) VALUES
    (1, 2),
    (2, 2);

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));
