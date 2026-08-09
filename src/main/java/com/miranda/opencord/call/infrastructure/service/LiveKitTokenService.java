package com.miranda.opencord.call.infrastructure.service;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LiveKitTokenService {

    private final String apiKey = "devkey";
    private final String apiSecret = "super-secret-key-opencord-livekit-2026";

    public String generateJoinToken(UUID channelId, UUID userId, String username) {
        AccessToken token = new AccessToken(apiKey, apiSecret);

        token.setName(username);
        token.setIdentity(userId.toString());

        token.addGrants(new RoomJoin(true), new RoomName(channelId.toString()));

        return token.toJwt();
    }
}
