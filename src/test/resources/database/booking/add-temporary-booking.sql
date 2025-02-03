INSERT INTO bookings (id, check_in_date, check_out_date, accommodation_id, user_id, status_id, is_deleted) VALUES
    (3, CURRENT_DATE + INTERVAL '1 day', CURRENT_DATE + INTERVAL '1 month', 2, 2, 1, false);

SELECT SETVAL('bookings_id_seq', (SELECT MAX(id) FROM bookings));
