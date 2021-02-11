package org.got.takeaway.domain.game;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameResponse {
    private String message;
    private String opponent;
    private GameStatus status;
    private int value;
    private int play;
    private boolean isWin;
}
