package org.got.takeaway.unit.service;

import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.exceptions.InvalidMoveException;
import org.got.takeaway.exceptions.OpponentNotFoundException;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.service.Impl.GameServiceImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameServiceImplUnitTest {

    private final DataHelper dataHelper = DataHelper.getInstance();
    @Mock
    private GameServiceImpl gameService;

    @Test
    public void testStart() {
        when(gameService.start(anyString())).thenReturn(dataHelper.gameStartResponse());
        assertEquals(dataHelper.gameStartResponse(), gameService.start("kylin"));
        verify(gameService, times(1)).start("kylin");
    }

    @Test
    public void testNumber() {
        doNothing().when(gameService).number(anyInt(), anyString());
        gameService.number(24, "kylin");
        verify(gameService, times(1)).number(24, "kylin");
    }

    @Test
    public void testPlay() {
        doNothing().when(gameService).play(any(GameRequest.class), anyString());
        gameService.play(dataHelper.gameRequest(), "kylin");
        verify(gameService, times(1)).play(dataHelper.gameRequest(), "kylin");
    }

    @Test
    public void testStartPlayerNotFoundException() {
        when(gameService.start(anyString())).thenThrow(new PlayerNotFoundException());
        assertThrows(PlayerNotFoundException.class, () -> gameService.start("any"));
    }

    @Test
    public void testNumberPlayerNotFoundException() {
        doThrow(new PlayerNotFoundException()).when(gameService).number(anyInt(), anyString());
        assertThrows(PlayerNotFoundException.class, () -> gameService.number(1,"any"));
    }

    @Test
    public void testNumberOpponentNotFoundException() {
        doThrow(new OpponentNotFoundException()).when(gameService).number(anyInt(), anyString());
        assertThrows(OpponentNotFoundException.class, () -> gameService.number(1,"any"));
    }

    @Test
    public void testNumberInvalidMoveException() {
        doThrow(new InvalidMoveException()).when(gameService).number(anyInt(), anyString());
        assertThrows(InvalidMoveException.class, () -> gameService.number(1,"any"));
    }
}
