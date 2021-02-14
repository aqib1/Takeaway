package org.got.takeaway.service;

import org.got.takeaway.domain.player.Player;

import java.util.Optional;

public interface PlayerService {
    void save(Player player);

    Optional<Player> findByName(String name);

    Optional<Player> findAvailable(String name);

    boolean isExists(String name);

    void delete(String name);
}
