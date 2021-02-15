package org.got.takeaway.unit.configuration;

import org.got.takeaway.configuration.WebSocketEventListener;
import org.got.takeaway.exceptions.RequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WebSocketEventListenerUnitTest {
    @Mock
    private WebSocketEventListener listener;

    @Test
    public void testHandleWebSocketConnected() {
        doNothing().when(listener).handleWebSocketConnected(isNull());
        listener.handleWebSocketConnected(null);
        verify(listener, times(1)).handleWebSocketConnected(null);
    }

    @Test
    public void testHandleWebSocketDisconnected() {
        doNothing().when(listener).handleWebSocketDisconnected(isNull());
        listener.handleWebSocketDisconnected(null);
        verify(listener, times(1)).handleWebSocketDisconnected(null);
    }

    @Test
    public void testHandleWebSocketDisconnectedRequestException() {
        doThrow(new RequestException()).when(listener).handleWebSocketDisconnected(isNull());
        assertThrows(RequestException.class, () -> listener.handleWebSocketDisconnected(null));
    }
}
