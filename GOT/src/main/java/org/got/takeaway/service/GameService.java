package org.got.takeaway.service;


import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;

public interface GameService {
 GameResponse start(String playerName);
 void number(int number, String playerName);
 void play(GameRequest request, String playerName);
}
