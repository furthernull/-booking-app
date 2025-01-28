INSERT INTO accommodations (id, type_id, location_id, size, daily_rate, availability, is_deleted) VALUES
    (3, 3, 1, '2 Bedroom', 100.00, 1, false);

INSERT INTO accommodations_amenity_types (accommodation_id, amenity_id) VALUES (3, 7);