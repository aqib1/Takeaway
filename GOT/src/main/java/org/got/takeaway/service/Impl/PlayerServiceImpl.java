package org.got.takeaway.service.Impl;

import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.message.MessageFactory;
import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.repositories.PlayerRepository;
import org.got.takeaway.service.NotificationService;
import org.got.takeaway.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class PlayerServiceImpl implements PlayerService {

    private MessageFactory factory = MessageFactory.getInstance();

    @Autowired
    private PlayerRepository repository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void save(Player player) {
        repository.save(player);
    }

    @Override
    public Optional<Player> findByName(String name) {
        return ofNullable(repository.findByName(name));
    }

    @Override
    public Optional<Player> findAvailable(String name) {
        return ofNullable(repository.findAvailable(name));
    }

    @Override
    public void delete(String name) {
        ofNullable(repository.findByName(name))
                .ifPresentOrElse(player -> {
                    repository.delete(name);
                    ofNullable(player.getOpponent()).ifPresent(opponent -> {
                        updateAndNotifyOpponent(opponent);
                    });
                }, () -> {
                    throw new PlayerNotFoundException("Player not exists!!!");
                });
    }

    private void updateAndNotifyOpponent(Player player) {
        var disconnectResponse = factory.disconnectResponse(player.getOpponent().getName());
        player.setOpponent(null);
        player.setPlayerStatus(PlayerStatus.AVAILABLE);
        repository.save(player);
        notificationService.notifyPlayer(player.getName(),
                ResponseEntity.<GameResponse>builder().body(disconnectResponse)
                        .status(HttpStatus.OK).build()
        );
    }

    @Override
    public boolean isExists(String name) {
        return repository.isExists(name);
    }
}
