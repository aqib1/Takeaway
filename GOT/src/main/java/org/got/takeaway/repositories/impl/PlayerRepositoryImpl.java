package org.got.takeaway.repositories.impl;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.repositories.PlayerRepository;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private Map<String, Player> availablePlayer = new HashMap<>();

    @Override
    public void save(Player player) {
        availablePlayer.put(player.getName(), player);
    }

    @Override
    public Player findByName(String name) {
        return availablePlayer.get(name);
    }

    @Override
    public Player findAvailable(String name) {
        return availablePlayer.values().stream()
                .filter(x -> x.getPlayerStatus() == PlayerStatus.AVAILABLE && !x.getName().equals(name))
                .findFirst().orElse(null);
    }

    @Override
    public void delete(String name) {
        availablePlayer.remove(name);
    }

    @Override
    public boolean isExists(String name) {
        return availablePlayer.containsKey(name);
    }
}
