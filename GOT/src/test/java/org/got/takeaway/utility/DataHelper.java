package org.got.takeaway.utility;

import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.got.takeaway.utility.TestConst.*;

public class DataHelper {

    public ResponseEntity<GameResponse> gameStartResponseEntity() {
        return ResponseEntity.<GameResponse>builder().body(gameStartResponse()).status(HttpStatus.OK).build();
    }

    public ResponseEntity<GameResponse> gamePlayResponseEntity() {
        return ResponseEntity.<GameResponse>builder().body(gamePlayResponse()).status(HttpStatus.OK).build();
    }

    public ResponseEntity<GameResponse> gameWaitingResponseEntity() {
        return ResponseEntity.<GameResponse>builder().body(gameWaitingResponse()).status(HttpStatus.OK).build();
    }

    public ResponseEntity<GameResponse> gameDisconnectResponseEntity() {
        return ResponseEntity.<GameResponse>builder().body(gameDisconnectResponse()).status(HttpStatus.OK).build();
    }

    public ResponseEntity<GameResponse> gameOverResponseEntity() {
        return ResponseEntity.<GameResponse>builder().body(gameOverResponse()).status(HttpStatus.OK).build();
    }

    public Optional<Player> optionalPlayer() {
        return ofNullable(player());
    }

    public GameRequest gameRequest() {
        return GameRequest.builder()
                .number(30)
                .moveAttr(1)
                .build();
    }

    public GameResponse gameOverResponse() {
        return GameResponse.builder()
                .status(GameStatus.OVER)
                .message("Game over")
                .isWin(true)
                .number(100)
                .primary(false)
                .opponent("Csaba")
                .build();
    }

    public GameResponse gameDisconnectResponse() {
        return GameResponse.builder()
                .status(GameStatus.DISCONNECT)
                .message("Aqib disconnected from game")
                .isWin(false)
                .number(0)
                .primary(false)
                .opponent(null)
                .build();
    }

    public GameResponse gameWaitingResponse() {
        return GameResponse.builder()
                .status(GameStatus.WAITING)
                .message("Waiting for opponent")
                .isWin(false)
                .number(100)
                .primary(true)
                .opponent("Peter")
                .build();
    }

    public GameResponse gamePlayResponse() {
        return GameResponse.builder()
                .status(GameStatus.PLAY)
                .message("Game Play")
                .isWin(false)
                .number(25)
                .primary(false)
                .opponent("Zoltan")
                .build();
    }

    public GameResponse gameStartResponse() {
        return GameResponse.builder()
                .status(GameStatus.START)
                .message("Game started")
                .isWin(false)
                .number(24)
                .primary(true)
                .opponent("kylin")
                .build();
    }

    public Player player() {
        return new Player("Ahmad", null, true, PlayerStatus.AVAILABLE);
    }

    public Player player(String name) {
        return new Player(name, null, false, PlayerStatus.AVAILABLE);
    }
    public Player pairedPlayer(String name) {
        return new Player(name, null, false, PlayerStatus.PAIRED);
    }

    public Principal getPrincipal() {
        return () -> "Aqib";
    }

    public Map<String, String> getSessionMap() {
        return Map.of(USERNAME, PLAYER_1);
    }

    public Message<byte[]> message() {
        return MessageBuilder.withPayload(new byte[0])
                .setHeader(StompHeaderAccessor.SESSION_ATTRIBUTES, getSessionMap())
                .build();
    }

    public SessionDisconnectEvent sessionDisconnectEvent() {
        return new SessionDisconnectEvent(new Object(), message(), SESSION_ID, CloseStatus.NORMAL);
    }

    public GameStatus [] getGameStatus() {
        return new GameStatus[] {
                GameStatus.START,
                GameStatus.PLAY,
                GameStatus.WAITING,
                GameStatus.DISCONNECT,
                GameStatus.OVER
        };
    }

    public PlayerStatus [] getPlayerStatus() {
        return new PlayerStatus[] {
                PlayerStatus.PAIRED,
                PlayerStatus.AVAILABLE
        };
    }


    /**
     * Initialization on demand pattern
     * this pattern is alternative of double check locking pattern
     * which not even support lazy loading but also safe to use in
     * multi-processor distributed instances
     */

    private static class InstanceHolder {
        private static final DataHelper INSTANCE = new DataHelper();

        private InstanceHolder() {

        }
    }


    public static DataHelper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private DataHelper() {

    }
}
