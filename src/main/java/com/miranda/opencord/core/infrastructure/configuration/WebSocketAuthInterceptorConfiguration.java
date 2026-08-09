package com.miranda.opencord.core.infrastructure.configuration;

import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import com.miranda.opencord.user.infrastructure.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptorConfiguration implements ChannelInterceptor {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ChannelService channelService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            if (authorization == null || authorization.isEmpty()) {
                throw new IllegalArgumentException("Acesso Negado: Token ausente.");
            }
            String token = authorization.get(0).replace("Bearer ", "");
            String email = tokenService.validateToken(token);
            if (email.isEmpty()) throw new IllegalArgumentException("Acesso Negado: Token inválido.");

            UserEntity user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
            var auth = new UsernamePasswordAuthenticationToken(user.getId().toString(), null, Collections.emptyList());
            auth.setDetails(user);
            accessor.setUser(auth);
        }

        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/topic/channel.")) {
                String channelIdStr = destination.replace("/topic/channel.", "");
                UUID channelId = UUID.fromString(channelIdStr);

                UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) accessor.getUser();
                if (auth == null) throw new IllegalArgumentException("Usuário não autenticado no STOMP.");

                UserEntity user = (UserEntity) auth.getDetails();

                if (user == null) {
                    throw new UserNotFound();
                }

                boolean isMember = channelService.isUserMemberOf(user.getId(), channelId);
                if (!isMember) {
                    throw new IllegalArgumentException("Acesso Negado: Você não é membro deste canal.");
                }
            }
        }
        return message;
    }
}