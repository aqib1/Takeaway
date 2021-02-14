package org.got.takeaway.utility;

import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public class DataHelper {

    public ResponseEntity<GameResponse> gameStartResponseEntity() {
        return ResponseEntity.ok(gameStartResponse());
    }

    public ResponseEntity<GameResponse> gamePlayResponseEntity() {
        return ResponseEntity.ok(gamePlayResponse());
    }

    public ResponseEntity<GameResponse> gameWaitingResponseEntity() {
        return ResponseEntity.ok(gameWaitingResponse());
    }

    public ResponseEntity<GameResponse> gameDisconnectResponseEntity() {
        return ResponseEntity.ok(gameDisconnectResponse());
    }

    public ResponseEntity<GameResponse> gameOverResponseEntity() {
        return ResponseEntity.ok(gameOverResponse());
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
                .status(GameStatus.WAITING)
                .message("Waiting for opponent")
                .isWin(false)
                .number(100)
                .primary(true)
                .opponent("Peter")
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

    public Principal getPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return "Aqib";
            }
        };
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
