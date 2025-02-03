INSERT INTO addresses (id, address, city, state, zip_code, country, is_deleted) VALUES
    (1, 'Address', 'City', 'State', '12345', 'Country', false);

SELECT SETVAL('addresses_id_seq', (SELECT MAX(id) FROM addresses));

INSERT INTO accommodation_types (id, name) VALUES
                                               (1, 'APARTMENT'),
                                               (2, 'CONDO'),
                                               (3, 'HOUSE');

INSERT INTO amenity_types (id, name) VALUES
                                         (1, 'PARKING'),
                                         (2, 'CAR_CHARGER'),
                                         (3, 'PETS'),
                                         (4, 'WI_FI'),
                                         (5, 'POOL'),
                                         (6, 'SPA'),
                                         (7, 'GYM'),
                                         (8, 'CAFE');

INSERT INTO accommodations (id, type_id, location_id, size, daily_rate, availability, is_deleted) VALUES
    (1, 1, 1, 'Studio', 10.00, 1, false),
    (2, 2, 1, '1 Bedroom,', 10.00, 1, false);

SELECT SETVAL('accommodations_id_seq', (SELECT MAX(id) FROM accommodations));

INSERT INTO accommodations_amenity_types (accommodation_id, amenity_id) VALUES
                                                                            (1, 1),
                                                                            (1, 2),
                                                                            (1, 3),
                                                                            (1, 4),
                                                                            (1, 5),
                                                                            (1, 6),
                                                                            (1, 7),
                                                                            (1, 8),
                                                                            (2, 1),
                                                                            (2, 2),
                                                                            (2, 3),
                                                                            (2, 4),
                                                                            (2, 5),
                                                                            (2, 6),
                                                                            (2, 7),
                                                                            (2, 8);

INSERT INTO users (id, email, first_name, last_name, password, is_deleted) VALUES
    (1, 'john.example@email.com', 'John', 'Doe', '$2a$10$yvofprfFFpxZatoflOuduupLzGqFWizREUJ0.lcQMDREJ28r8XB6.', false),
    (2, 'jane.example@gmail.com', 'Jane', 'Doe', '$2a$10$yvofprfFFpxZatoflOuduupLzGqFWizREUJ0.lcQMDREJ28r8XB6.', false);

INSERT INTO users_roles (user_id, role_id) VALUES
                                               (1, 2),
                                               (2, 2);

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO booking_statuses (id, status) VALUES
    (1, 'PENDING'),
    (2, 'CONFIRMED'),
    (3, 'CANCELLED'),
    (4, 'EXPIRED');

INSERT INTO bookings (id, check_in_date, check_out_date, accommodation_id, user_id, status_id, is_deleted) VALUES
    (1, CURRENT_DATE + INTERVAL '1 day', CURRENT_DATE + INTERVAL '1 month', 1, 1, 1, false),
    (2, CURRENT_DATE + INTERVAL '2 month', CURRENT_DATE + INTERVAL '1 year', 1, 1, 1, false);

SELECT SETVAL('bookings_id_seq', (SELECT MAX(id) FROM bookings));
