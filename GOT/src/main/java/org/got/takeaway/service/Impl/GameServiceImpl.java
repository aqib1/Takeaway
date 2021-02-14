package org.got.takeaway.service.Impl;

import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.message.MessageFactory;
import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.exceptions.InvalidNumberException;
import org.got.takeaway.exceptions.OpponentNotFoundException;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.got.takeaway.utils.AppConst.DIVISOR;

@Service
public class GameServiceImpl implements GameService {

    MessageFactory messageFactory = MessageFactory.getInstance();

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public GameResponse start(String playerName) {
        return playerService.findByName(playerName)
                .map(this::startRequestForPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(String.format("[%s] player not exists in system!!", playerName)));
    }

    @Override
    public void number(int number, String playerName) {
        playerService.findByName(playerName)
                .ifPresentOrElse((player) -> {
                            verifyOpponent(player);
                            notificationService.notifyPlayer(player.getOpponent().getName(),
                                    ResponseEntity.ok(messageFactory.playResponse(number, player)));
                        },
                        () -> new PlayerNotFoundException(String.format("[%s] player not exists in system!!", playerName)));

    }

    @Override
    public void play(GameRequest request, String playerName) {
        playerService.findByName(playerName)
                .ifPresentOrElse(player -> {
                    verifyOpponent(player);
                    int addition = request.getNumber() + request.getMoveAttr();
                    checkDivisible(addition);
                    int newVal = addition / DIVISOR;
                    if(newVal != 1) {
                        notificationService.notifyPlayer(player.getOpponent().getName(),
                                ResponseEntity.ok(messageFactory.playResponse(newVal, player)));
                    } else {
                        notificationService.notifyPlayer(player.getName(), ResponseEntity.ok(messageFactory.gameOverResponse(true)));
                        notificationService.notifyPlayer(player.getOpponent().getName(), ResponseEntity.ok(messageFactory.gameOverResponse(false)));
                    }

                },() -> new PlayerNotFoundException(String.format("[%s] player not exists in system!!", playerName)));

    }

    private void checkDivisible(int addition) {
        if(addition % DIVISOR != 0) {
            throw new InvalidNumberException(String.format("%d is not divisible by %d", addition, DIVISOR));
        }
    }

    private void verifyOpponent(Player player) {
        ofNullable(player.getOpponent()).orElseThrow(() -> {
            throw new OpponentNotFoundException("Opponent not exists!!!");
        });
    }

    private GameResponse startRequestForPlayer(Player player) {
        return createPairWithAvailablePlayer(player)
                .orElseGet(() -> MessageFactory.getInstance().waitingResponse());
    }

    private Optional<GameResponse> createPairWithAvailablePlayer(Player player) {
        return playerService.findAvailable(player.getName())
                .map(availablePlayer -> {
                    availablePlayer.setPrimary(false);
                    // make it primary for first input rights
                    player.setPrimary(true);
                    // make them paired
                    availablePlayer.setPlayerStatus(PlayerStatus.PAIRED);
                    player.setPlayerStatus(PlayerStatus.PAIRED);
                    availablePlayer.setOpponent(player);
                    player.setOpponent(availablePlayer);
                    // notify user about their opponents
                    notificationService.notifyPlayer(availablePlayer.getName(),
                            ResponseEntity.ok(messageFactory.startingResponse(availablePlayer.getOpponent())));
                    return MessageFactory.getInstance().startingResponse(player.getOpponent());
                });
    }
}
