CREATE TABLE servers (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         icon_url VARCHAR(255),
                         owner_id UUID NOT NULL,
                         CONSTRAINT fk_servers_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE server_members (
                                id UUID PRIMARY KEY,
                                server_id UUID NOT NULL,
                                user_id UUID NOT NULL,
                                role VARCHAR(50) NOT NULL,
                                CONSTRAINT fk_server_members_server FOREIGN KEY (server_id) REFERENCES servers(id) ON DELETE CASCADE,
                                CONSTRAINT fk_server_members_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE channels
    ADD COLUMN server_id UUID;

ALTER TABLE channels
    ADD CONSTRAINT fk_channels_server
        FOREIGN KEY (server_id) REFERENCES servers(id) ON DELETE CASCADE;