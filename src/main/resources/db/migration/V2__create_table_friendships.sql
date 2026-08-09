CREATE TABLE friendships
(
    id           UUID                           NOT NULL,
    requester_id UUID                           NOT NULL,
    addressee_id UUID                           NOT NULL,
    status       VARCHAR(255)                   NOT NULL,
    created_at   TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_friendships PRIMARY KEY (id)
);

ALTER TABLE friendships
    ADD CONSTRAINT FK_FRIENDSHIPS_ON_ADDRESSEE FOREIGN KEY (addressee_id) REFERENCES users (id);

ALTER TABLE friendships
    ADD CONSTRAINT FK_FRIENDSHIPS_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id);