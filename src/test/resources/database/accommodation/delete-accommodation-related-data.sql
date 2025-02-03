DELETE FROM accommodations_amenity_types;
DELETE FROM accommodations;
DELETE FROM amenity_types;
DELETE FROM accommodation_types;
DELETE FROM addresses;
ALTER SEQUENCE addresses_id_seq RESTART WITH 1;
ALTER SEQUENCE accommodations_id_seq RESTART WITH 1;
