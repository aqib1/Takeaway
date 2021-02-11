package org.got.takeaway.service;

import org.got.takeaway.domain.game.GameResponse;

public interface NotificationService {
    void notifyPlayer(String playerName, GameResponse response);
}
