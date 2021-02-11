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
                .opponent(player.getOpponent().getName())
                .message(String.format("%s requested a match", player.getOpponent().getName()))
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
