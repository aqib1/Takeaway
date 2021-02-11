package org.got.takeaway.configuration;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private PlayerRepository repository;

    @EventListener
    public void handleWebSocketConnected(SessionConnectedEvent event) {
        Player newPlayer = new Player();
        newPlayer.setName(event.getUser().getName());
        newPlayer.setPlayerStatus(PlayerStatus.AVAILABLE);
        repository.save(newPlayer);
    }


    @EventListener
    public void handleWebSocketDisconnected(SessionDisconnectEvent event) {

    }
}
