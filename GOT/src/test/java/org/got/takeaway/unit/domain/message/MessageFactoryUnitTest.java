package org.got.takeaway.unit.domain.message;

import org.got.takeaway.domain.message.MessageFactory;
import org.got.takeaway.domain.player.Player;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageFactoryUnitTest {
    private DataHelper dataHelper = DataHelper.getInstance();
    @Mock
    private MessageFactory messageFactory;

    @Test
    public void testStartResponse() {
        when(messageFactory.startingResponse(any(Player.class)))
                .thenReturn(dataHelper.gameStartResponse());
        assertEquals(messageFactory.startingResponse(dataHelper.player()), dataHelper.gameStartResponse());
        verify(messageFactory, times(1)).startingResponse(dataHelper.player());
    }

    @Test
    public void testWaitingResponse() {
        when(messageFactory.waitingResponse())
                .thenReturn(dataHelper.gameWaitingResponse());
        assertEquals(messageFactory.waitingResponse(), dataHelper.gameWaitingResponse());
        verify(messageFactory, times(1)).waitingResponse();
    }

    @Test
    public void testPlayResponse() {
        when(messageFactory.playResponse(anyInt(), any(Player.class)))
                .thenReturn(dataHelper.gamePlayResponse());
        assertEquals(dataHelper.gamePlayResponse(), messageFactory.playResponse(0, dataHelper.player()));
        verify(messageFactory, times(1)).playResponse(0, dataHelper.player());
    }

    @Test
    public void testGameOverResponse() {
        when(messageFactory.gameOverResponse(anyBoolean()))
                .thenReturn(dataHelper.gameOverResponse());
        assertEquals(dataHelper.gameOverResponse(), messageFactory.gameOverResponse(true));
        verify(messageFactory, times(1)).gameOverResponse(true);
    }

    @Test
    public void testGameDisconnectResponse() {
        when(messageFactory.disconnectResponse(anyString()))
                .thenReturn(dataHelper.gameDisconnectResponse());
        assertEquals(dataHelper.gameDisconnectResponse(), messageFactory.disconnectResponse("Peter"));
        verify(messageFactory, times(1)).disconnectResponse("Peter");
    }
}
