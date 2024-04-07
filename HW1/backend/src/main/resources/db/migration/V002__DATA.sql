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
VALUES (1, '2024-04-08 08:00:00', '2024-04-08 06:00:00', 50, 10.00, 3, 1, 1),
       (2, '2024-04-09 10:00:00', '2024-04-09 09:00:00', 30, 8.50, 4, 2, 3),
       (3, '2024-04-09 12:00:00', '2024-04-09 11:00:00', 20, 12.00, 1, 3, 2),
       (4, '2024-04-10 14:00:00', '2024-04-10 13:00:00', 40, 15.50, 2, 4, 4),
       (5, '2024-04-11 16:00:00', '2024-04-11 15:00:00', 50, 9.00, 3, 1, 4),
       (6, '2024-04-11 18:00:00', '2024-04-11 17:00:00', 30, 11.00, 2, 2, 1),
       (7, '2024-04-12 20:00:00', '2024-04-12 19:00:00', 20, 7.50, 1, 3, 3),
       (8, '2024-04-13 22:00:00', '2024-04-13 21:00:00', 40, 5.00, 2, 4, 2),
       (9, '2024-04-14 00:00:00', '2024-04-13 23:00:00', 50, 20.00, 3, 1, 1),
       (10, '2024-04-15 08:00:00', '2024-04-15 06:00:00', 50, 10.00, 3, 1, 1),
       (11, '2024-04-16 10:00:00', '2024-04-16 09:00:00', 30, 8.50, 4, 2, 3),
       (12, '2024-04-16 12:00:00', '2024-04-16 11:00:00', 20, 12.00, 1, 3, 2),
       (13, '2024-04-17 14:00:00', '2024-04-17 13:00:00', 40, 15.50, 2, 4, 4),
       (14, '2024-04-18 16:00:00', '2024-04-18 15:00:00', 50, 9.00, 3, 1, 4),
       (15, '2024-04-18 18:00:00', '2024-04-18 17:00:00', 30, 11.00, 4, 2, 1),
       (16, '2024-04-19 20:00:00', '2024-04-19 19:00:00', 20, 7.50, 1, 3, 3),
       (17, '2024-04-20 22:00:00', '2024-04-20 21:00:00', 40, 5.00, 2, 4, 2),
       (18, '2024-04-21 00:00:00', '2024-04-20 23:00:00', 50, 20.00, 3, 1, 1),
       (19, '2024-04-22 08:00:00', '2024-04-22 06:00:00', 50, 10.00, 3, 1, 1),
       (20, '2024-04-23 10:00:00', '2024-04-23 09:00:00', 30, 8.50, 4, 2, 3),
       (21, '2024-04-08 12:00:00', '2024-04-08 11:00:00', 20, 12.00, 4, 3, 1),
       (22, '2024-04-09 14:00:00', '2024-04-09 13:00:00', 40, 15.50, 1, 4, 2),
       (23, '2024-04-09 16:00:00', '2024-04-09 15:00:00', 50, 9.00, 3, 1, 3),
       (24, '2024-04-09 18:00:00', '2024-04-09 17:00:00', 30, 11.00, 2, 2, 1),
       (25, '2024-04-10 20:00:00', '2024-04-10 19:00:00', 20, 7.50, 3, 3, 4),
       (26, '2024-04-11 22:00:00', '2024-04-11 21:00:00', 40, 5.00, 4, 4, 3),
       (27, '2024-04-11 00:00:00', '2024-04-10 23:00:00', 50, 20.00, 1, 1, 2),
       (28, '2024-04-11 08:00:00', '2024-04-11 06:00:00', 50, 10.00, 2, 2, 4),
       (29, '2024-04-12 10:00:00', '2024-04-12 09:00:00', 30, 8.50, 3, 3, 1),
       (30, '2024-04-12 12:00:00', '2024-04-12 11:00:00', 20, 12.00, 4, 4, 2),
       (31, '2024-04-13 14:00:00', '2024-04-13 13:00:00', 40, 15.50, 1, 1, 3),
       (32, '2024-04-14 16:00:00', '2024-04-14 15:00:00', 50, 9.00, 2, 2, 4),
       (33, '2024-04-14 18:00:00', '2024-04-14 17:00:00', 30, 11.00, 3, 3, 1),
       (34, '2024-04-15 20:00:00', '2024-04-15 19:00:00', 20, 7.50, 4, 4, 2),
       (35, '2024-04-16 22:00:00', '2024-04-16 21:00:00', 40, 5.00, 1, 1, 3),
       (36, '2024-04-16 00:00:00', '2024-04-15 23:00:00', 50, 20.00, 2, 2, 4),
       (37, '2024-04-17 08:00:00', '2024-04-17 06:00:00', 50, 10.00, 3, 3, 1),
       (38, '2024-04-18 10:00:00', '2024-04-18 09:00:00', 30, 8.50, 4, 4, 2),
       (39, '2024-04-18 12:00:00', '2024-04-18 11:00:00', 20, 12.00, 1, 1, 3),
       (40, '2024-04-19 14:00:00', '2024-04-19 13:00:00', 40, 15.50, 2, 2, 4),
       (41, '2024-04-20 16:00:00', '2024-04-20 15:00:00', 50, 9.00, 3, 3, 1),
       (42, '2024-04-20 18:00:00', '2024-04-20 17:00:00', 30, 11.00, 4, 4, 2),
       (43, '2024-04-21 20:00:00', '2024-04-21 19:00:00', 20, 7.50, 1, 1, 3),
       (44, '2024-04-22 22:00:00', '2024-04-22 21:00:00', 40, 5.00, 2, 2, 4),
       (45, '2024-04-22 00:00:00', '2024-04-21 23:00:00', 50, 20.00, 3, 3, 1),
       (46, '2024-04-23 08:00:00', '2024-04-23 06:00:00', 50, 10.00, 4, 4, 2),
       (47, '2024-04-24 10:00:00', '2024-04-24 09:00:00', 30, 8.50, 1, 1, 3),
       (48, '2024-04-24 12:00:00', '2024-04-24 11:00:00', 20, 12.00, 2, 2, 4),
       (49, '2024-04-25 14:00:00', '2024-04-25 13:00:00', 40, 15.50, 3, 3, 1),
       (50, '2024-04-26 16:00:00', '2024-04-26 15:00:00', 50, 9.00, 4, 4, 2),
       (51, '2024-04-08 08:00:00', '2024-04-08 06:00:00', 50, 10.00, 3, 1, 1),
       (52, '2024-04-08 10:00:00', '2024-04-08 09:00:00', 30, 8.50, 2, 2, 4);


-- Set serial sequence
SELECT pg_catalog.setval('public.bus_id_seq', 4, true);
SELECT pg_catalog.setval('public.city_id_seq', 4, true);
SELECT pg_catalog.setval('public.trip_id_seq', 18, true);
