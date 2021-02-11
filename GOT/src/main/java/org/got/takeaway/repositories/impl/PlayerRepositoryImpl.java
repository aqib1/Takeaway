package org.got.takeaway.repositories.impl;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.repositories.PlayerRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

import static java.util.Optional.ofNullable;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private Map<String, Player> availablePlayer = new HashMap<>();

    @Override
    public void save(Player player) {
        availablePlayer.put(player.getName(), player);
    }

    @Override
    public Optional<Player> findByName(String name) {
        return ofNullable(availablePlayer.get(name));
    }

    @Override
    public Optional<Player> findAvailable(String name) {
        return ofNullable(availablePlayer.values().stream()
                .filter(x -> x.getPlayerStatus() == PlayerStatus.AVAILABLE && !x.getName().equals(name))
                .findFirst().orElse(null));
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
