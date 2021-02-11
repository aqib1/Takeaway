package org.got.takeaway.domain.game;

import lombok.Getter;

@Getter
public enum GameStatus {
    START("S"), PLAYING("P"), WAITING("W"), DISCONNECT("D"), OVER("O");
    private String key;
    private GameStatus(String key) {
        this.key = key;
    }
}
