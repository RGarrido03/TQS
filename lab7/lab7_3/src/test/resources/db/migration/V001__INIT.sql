CREATE TABLE BOOK
(
    id     BIGSERIAL PRIMARY KEY,
    title  VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year   INTEGER      NOT NULL
);
