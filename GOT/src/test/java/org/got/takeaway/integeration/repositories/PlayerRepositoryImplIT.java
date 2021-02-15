package org.got.takeaway.integeration.repositories;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerRepositoryImplIT {

    private final DataHelper helper = DataHelper.getInstance();
    private PlayerRepositoryImpl repository;

    @BeforeEach
    public void setup() {
        repository = new PlayerRepositoryImpl();
    }

    @Test
    public void findByName_Should_ReturnPlayerWithName() {
        // given
        var test1 = helper.player("test1");
        repository.save(test1);

        // when
        Player playerByName = repository.findByName("test1");
        Player noneExistingPlayer = repository.findByName("Khaleesi");

        //then
        Assert.assertNotNull(playerByName);
        Assert.assertEquals("test1", playerByName.getName());

        Assert.assertNull(noneExistingPlayer);
    }

    @Test
    public void delete_Should_RemovePlayerFromStorage() {
        // given
        var player = helper.player("test1");
        repository.save(player);

        Assert.assertNotNull(player);
        Assert.assertEquals("test1", player.getName());
        // when
        repository.delete(player.getName());

        // then
        Assert.assertNull(repository.findByName("test1"));
    }

    @Test
    public void exist_Should_ReturnTrueIfPlayerWithNameExists() {
        // given / when
        repository.save(helper.player("test1"));

        // then
        Assert.assertTrue(repository.isExists("test1"));
        Assert.assertFalse(repository.isExists("test2"));
    }

    @Test
    public void findAvailable_Should_ReturnPlayerIfAvailable() {
        // given
        var player = helper.player("test1");

        repository.save(player);

        // when
        Player available = repository.findAvailable("Drogo");

        // then
        Assert.assertNotNull(available);
        Assert.assertEquals("test1", available.getName());
    }

    @Test
    public void findAvailable_Should_NotReturnWhenPlayerIsPaired() {
        //given
        var player = helper.player("test1");
        player.setPlayerStatus(PlayerStatus.PAIRED);
        repository.save(player);

        // when
        Player available = repository.findAvailable("test2");

        // then
        Assert.assertNull(available);
    }

    @Test
    public void findAvailable_Should_NotReturnRequestingPlayer() {
        //given
        var player = helper.player("test1");
        repository.save(player);

        // when
        Player available = repository.findAvailable("test1");

        // then
        Assert.assertNull(available);
    }
}
