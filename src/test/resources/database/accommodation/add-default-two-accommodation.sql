INSERT INTO addresses (id, address, city, state, zip_code, country, is_deleted) VALUES
    (1, 'Address', 'City', 'State', '12345', 'Country', false);

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
