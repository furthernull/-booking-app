INSERT INTO users (id, email, first_name, last_name, password, is_deleted) VALUES
    (1, 'john.example@email.com', 'John', 'Doe', '$2a$10$4c3FoiAa1RINf6IMfJ.MXOPKkbapFI/EWuuLq2QCR2GHGj9FDTYke', false),
    (2, 'jane.example@gmail.com', 'Jane', 'Doe', '$2a$10$4c3FoiAa1RINf6IMfJ.MXOPKkbapFI/EWuuLq2QCR2GHGj9FDTYke', false);

INSERT INTO users_roles (user_id, role_id) VALUES
    (1, 2),
    (2, 2);
