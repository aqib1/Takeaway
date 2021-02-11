package org.got.takeaway.domain.player;

import lombok.Getter;

@Getter
public enum PlayerStatus {
    PAIRED("P"), AVAILABLE("A");
    private String key;

    private PlayerStatus(String key) {

    }
}
