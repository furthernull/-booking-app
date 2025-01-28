INSERT INTO bookings (id, check_in_date, check_out_date, accommodation_id, user_id, status_id, is_deleted) VALUES
    (1, CURRENT_DATE + INTERVAL '1 day', CURRENT_DATE + INTERVAL '1 month', 1, 1, 1, false),
    (2, CURRENT_DATE + INTERVAL '2 month', CURRENT_DATE + INTERVAL '1 year', 1, 1, 1, false);
