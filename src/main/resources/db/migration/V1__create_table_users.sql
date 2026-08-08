CREATE TABLE users
(
    id              UUID                           NOT NULL,
    username        VARCHAR(255)                   NOT NULL,
    email           VARCHAR(255)                   NOT NULL,
    hashed_password VARCHAR(255)                   NOT NULL,
    created_at      TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);