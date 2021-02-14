package org.got.takeaway.domain.game;

import lombok.Builder;
import lombok.Getter;
import org.got.takeaway.domain.base.BaseResponse;

@Builder
@Getter
public class GameResponse extends BaseResponse {
    private String message;
    private String opponent;
    private GameStatus status;
    private boolean primary;
    private int number;
    private boolean isWin;
}
