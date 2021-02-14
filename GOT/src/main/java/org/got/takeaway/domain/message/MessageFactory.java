package org.got.takeaway.domain.message;

import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.domain.player.Player;

public class MessageFactory {
    private MessageFactory() {

    }
    public GameResponse waitingResponse() {
        return GameResponse.builder()
                .status(GameStatus.WAITING)
                .message("Waiting for player")
                .build();
    }

    public GameResponse startingResponse(Player player) {
        return GameResponse.builder()
                .status(GameStatus.START)
                .opponent(player.getName())
                .primary(player.isPrimary())
                .message(String.format("[You're playing against: <span>%s</span>]", player.getName()))
                .build();
    }

    public GameResponse playResponse(int number, Player player) {
        return GameResponse.builder()
                .status(GameStatus.PLAY)
                .opponent(player.getName())
                .number(number)
                .message(String.format("%s player sent number %d", player.getName(), number))
                .build();
    }

    public GameResponse gameOverResponse(boolean winner) {
        return GameResponse.builder()
                .status(GameStatus.OVER)
                .isWin(winner)
                .build();
    }

    public GameResponse disconnectResponse(String player) {
        return GameResponse.builder()
                .status(GameStatus.DISCONNECT)
                .message(String.format("%s disconnected from game", player))
                .build();
    }

    /**
     * Initialization on demand holder pattern
     * */

    private static class MessageFactoryHolder {
       private static final MessageFactory INSTANCE = new MessageFactory();
    }

    public static MessageFactory getInstance() {
        return MessageFactoryHolder.INSTANCE;
    }
}
