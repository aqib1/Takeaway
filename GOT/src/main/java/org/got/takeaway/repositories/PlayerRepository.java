package org.got.takeaway.repositories;

import org.got.takeaway.domain.player.Player;
import java.util.Optional;

public interface PlayerRepository {
    void save(Player player);
    Optional<Player> findByName(String name);
    Optional<Player> findAvailable(String name);
    void delete(String name);
    boolean isExists(String name);
}
