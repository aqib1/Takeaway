package org.got.takeaway.unit.domain.player;

import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerStatusUnitTest {

    private DataHelper dataHelper = DataHelper.getInstance();
    @Test
    public void testGameStatus() {
        PlayerStatus []statuses = PlayerStatus.values();
        assertTrue(statuses.length == 2);
        assertArrayEquals(dataHelper.getPlayerStatus(), statuses);
    }
}
