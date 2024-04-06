-- Insert default data
INSERT INTO public.bus (id, capacity)
VALUES (1, 50),
       (2, 30),
       (3, 20),
       (4, 40);

INSERT INTO public.city (id, name)
VALUES (1, 'Murtosa'),
       (2, 'Estarreja'),
       (3, 'Aveiro'),
       (4, 'Ovar');

INSERT INTO public.trip (id, arrival_time, departure_time, free_seats, price, arrival_id, bus_id, departure_id)
VALUES (1, '2024-04-06 08:00:00', '2024-04-06 06:00:00', 50, 10.00, 3, 1, 1),
       (2, '2024-04-06 10:00:00', '2024-04-06 09:00:00', 30, 8.50, 4, 2, 3),
       (3, '2024-04-06 12:00:00', '2024-04-06 11:00:00', 20, 12.00, 1, 3, 2),
       (4, '2024-04-06 14:00:00', '2024-04-06 13:00:00', 40, 15.50, 2, 4, 4),
       (5, '2024-04-06 16:00:00', '2024-04-06 15:00:00', 50, 9.00, 3, 1, 4),
       (6, '2024-04-06 18:00:00', '2024-04-06 17:00:00', 30, 11.00, 4, 2, 1),
       (7, '2024-04-06 20:00:00', '2024-04-06 19:00:00', 20, 7.50, 1, 3, 3),
       (8, '2024-04-06 22:00:00', '2024-04-06 21:00:00', 40, 5.00, 2, 4, 2),
       (9, '2024-04-06 00:00:00', '2024-04-05 23:00:00', 50, 20.00, 3, 1, 1),
       (10, '2024-04-07 08:00:00', '2024-04-07 06:00:00', 50, 10.00, 3, 1, 1),
       (11, '2024-04-07 10:00:00', '2024-04-07 09:00:00', 30, 8.50, 4, 2, 3),
       (12, '2024-04-07 12:00:00', '2024-04-07 11:00:00', 20, 12.00, 1, 3, 2),
       (13, '2024-04-07 14:00:00', '2024-04-07 13:00:00', 40, 15.50, 2, 4, 4),
       (14, '2024-04-07 16:00:00', '2024-04-07 15:00:00', 50, 9.00, 3, 1, 4),
       (15, '2024-04-07 18:00:00', '2024-04-07 17:00:00', 30, 11.00, 4, 2, 1),
       (16, '2024-04-07 20:00:00', '2024-04-07 19:00:00', 20, 7.50, 1, 3, 3),
       (17, '2024-04-07 22:00:00', '2024-04-07 21:00:00', 40, 5.00, 2, 4, 2),
       (18, '2024-04-07 00:00:00', '2024-04-06 23:00:00', 50, 20.00, 3, 1, 1);


-- Set serial sequence
SELECT pg_catalog.setval('public.bus_id_seq', 4, true);
SELECT pg_catalog.setval('public.city_id_seq', 4, true);
SELECT pg_catalog.setval('public.trip_id_seq', 18, true);
