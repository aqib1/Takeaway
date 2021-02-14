package org.got.takeaway.repositories;

import org.got.takeaway.domain.player.Player;

public interface PlayerRepository {
    void save(Player player);

    Player findByName(String name);

    Player findAvailable(String name);

    void delete(String name);

    boolean isExists(String name);
}
