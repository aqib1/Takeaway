package org.got.takeaway.configuration;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.exceptions.RequestException;
import org.got.takeaway.service.Impl.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static java.util.Optional.ofNullable;
import static org.got.takeaway.utils.AppConst.USERNAME;

@Component
public class WebSocketEventListener {

    @Autowired
    private PlayerServiceImpl playerService;

    @EventListener
    public void handleWebSocketConnected(SessionConnectedEvent event) {
        Player newPlayer = new Player();
        newPlayer.setName(event.getUser().getName());
        newPlayer.setPlayerStatus(PlayerStatus.AVAILABLE);
        playerService.save(newPlayer);
    }


    @EventListener
    public void handleWebSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        ofNullable(accessor.getSessionAttributes().get(USERNAME))
                .map(String.class::cast).ifPresentOrElse(playerService::delete, () -> {
                    throw new RequestException("Invalid disconnect request");
        });
    }
}
