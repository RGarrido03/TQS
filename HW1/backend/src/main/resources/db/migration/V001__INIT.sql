--
-- Name: bus; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bus
(
    id       bigint  NOT NULL,
    capacity integer NOT NULL
);

CREATE SEQUENCE public.bus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    OWNED BY public.bus.id;


--
-- Name: city; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.city
(
    id   bigint                 NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    OWNED BY public.city.id;


--
-- Name: reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reservation
(
    id      bigint  NOT NULL,
    price   double precision,
    seats   integer NOT NULL,
    trip_id bigint  NOT NULL,
    user_id bigint  NOT NULL
);

CREATE SEQUENCE public.reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    OWNED BY public.reservation.id;


--
-- Name: trip; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trip
(
    id             bigint NOT NULL,
    arrival_time   timestamp(6) without time zone,
    departure_time timestamp(6) without time zone,
    free_seats     integer,
    price          double precision,
    arrival_id     bigint NOT NULL,
    bus_id         bigint NOT NULL,
    departure_id   bigint NOT NULL
);

CREATE SEQUENCE public.trip_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    OWNED BY public.trip.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user"
(
    id       bigint                 NOT NULL,
    email    character varying(255) NOT NULL,
    name     character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    OWNED BY public."user".id;


-- Set default values
ALTER TABLE ONLY public.bus
    ALTER COLUMN id SET DEFAULT nextval('public.bus_id_seq'::regclass);

ALTER TABLE ONLY public.city
    ALTER COLUMN id SET DEFAULT nextval('public.city_id_seq'::regclass);

ALTER TABLE ONLY public.reservation
    ALTER COLUMN id SET DEFAULT nextval('public.reservation_id_seq'::regclass);

ALTER TABLE ONLY public.trip
    ALTER COLUMN id SET DEFAULT nextval('public.trip_id_seq'::regclass);

ALTER TABLE ONLY public."user"
    ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


-- Set primary keys
ALTER TABLE ONLY public.bus
    ADD CONSTRAINT bus_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


-- Set foreign keys
ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fka4ibleq81op3r57bis3rrfqrb FOREIGN KEY (user_id) REFERENCES public."user" (id);

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT fkaf40hxjlfpgfdvpdbfo6wavr5 FOREIGN KEY (arrival_id) REFERENCES public.city (id);

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fkbnq7qaveg8wkoee526abhsykn FOREIGN KEY (trip_id) REFERENCES public.trip (id);

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT fkf93mwkgkr04qfpabepe9o15e2 FOREIGN KEY (departure_id) REFERENCES public.city (id);

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT fkptvi61dd1hao1yig3in0gvcjs FOREIGN KEY (bus_id) REFERENCES public.bus (id);
