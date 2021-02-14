package org.got.takeaway.service;

import org.got.takeaway.domain.game.GameResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface NotificationService {
    void notifyPlayer(String name, ResponseEntity<GameResponse> response);
}
