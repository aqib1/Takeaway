package org.got.takeaway.service;


import org.got.takeaway.domain.game.GameResponse;

public interface GameService {
 GameResponse start(String playerName);
}
