package org.got.takeaway.service.Impl;

import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.message.MessageFactory;
import org.got.takeaway.domain.player.Player;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.got.takeaway.service.GameService;
import org.got.takeaway.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private PlayerRepositoryImpl playerRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public GameResponse start(String playerName) {
        return playerRepository.findByName(playerName)
                .map(this::startRequestForPlayer)
                .orElseThrow(() -> playerNotFoundException(playerName));
    }

    private GameResponse startRequestForPlayer(Player player) {
        return createPairWithAvailablePlayer(player)
                .orElseGet(() -> MessageFactory.getInstance().waitingResponse());
    }

    private Optional<GameResponse> createPairWithAvailablePlayer(Player player) {
        return playerRepository.findAvailable(player.getName())
                .map(availablePlayer -> {
                    availablePlayer.setOpponent(player);
                    player.setOpponent(availablePlayer);
                    notificationService.notifyPlayer(availablePlayer.getName()
                            , MessageFactory.getInstance().startingResponse(availablePlayer.getOpponent()));
                    return MessageFactory.getInstance().startingResponse(player);
                });
    }


    private PlayerNotFoundException playerNotFoundException(String playerName) {
        return new PlayerNotFoundException(playerName);
    }


}
