CREATE TABLE server_invites (
                                id UUID PRIMARY KEY,
                                code VARCHAR(50) NOT NULL UNIQUE,
                                server_id UUID NOT NULL,
                                inviter_id UUID NOT NULL,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                CONSTRAINT fk_server_invites_server FOREIGN KEY (server_id) REFERENCES servers(id) ON DELETE CASCADE,
                                CONSTRAINT fk_server_invites_inviter FOREIGN KEY (inviter_id) REFERENCES users(id) ON DELETE CASCADE
);