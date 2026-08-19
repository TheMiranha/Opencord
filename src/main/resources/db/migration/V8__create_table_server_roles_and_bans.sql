CREATE TABLE server_roles
(
    id          UUID                           NOT NULL,
    server_id   UUID                           NOT NULL,
    name        VARCHAR(100)                   NOT NULL,
    color       VARCHAR(20)                    DEFAULT '#99aab5',
    position    INT                            NOT NULL DEFAULT 0,
    permissions BIGINT                         NOT NULL DEFAULT 0,
    created_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_server_roles PRIMARY KEY (id),
    CONSTRAINT fk_server_roles_server FOREIGN KEY (server_id) REFERENCES servers (id) ON DELETE CASCADE
);

CREATE TABLE server_member_roles
(
    server_member_id UUID NOT NULL,
    role_id          UUID NOT NULL,
    CONSTRAINT pk_server_member_roles PRIMARY KEY (server_member_id, role_id),
    CONSTRAINT fk_smr_member FOREIGN KEY (server_member_id) REFERENCES server_members (id) ON DELETE CASCADE,
    CONSTRAINT fk_smr_role FOREIGN KEY (role_id) REFERENCES server_roles (id) ON DELETE CASCADE
);

CREATE TABLE server_bans
(
    id           UUID                           NOT NULL,
    server_id    UUID                           NOT NULL,
    user_id      UUID                           NOT NULL,
    reason       VARCHAR(255),
    banned_by_id UUID                           NOT NULL,
    created_at   TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_server_bans PRIMARY KEY (id),
    CONSTRAINT fk_server_bans_server FOREIGN KEY (server_id) REFERENCES servers (id) ON DELETE CASCADE,
    CONSTRAINT fk_server_bans_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_server_bans_banned_by FOREIGN KEY (banned_by_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_server_user_ban UNIQUE (server_id, user_id)
);
