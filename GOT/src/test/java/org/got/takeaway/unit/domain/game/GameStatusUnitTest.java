package org.got.takeaway.unit.domain.game;

import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStatusUnitTest {

    private DataHelper dataHelper = DataHelper.getInstance();
    @Test
    public void testGameStatus() {
        GameStatus []statuses = GameStatus.values();
        assertTrue(statuses.length == 5);
        assertArrayEquals(dataHelper.getGameStatus(), statuses);
    }
}
