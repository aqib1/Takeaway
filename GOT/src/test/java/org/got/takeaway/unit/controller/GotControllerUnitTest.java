package org.got.takeaway.unit.controller;

import org.got.takeaway.controller.GotController;
import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import java.security.Principal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GotControllerUnitTest {

    private final DataHelper dataHelper = DataHelper.getInstance();
    @Mock
    private GotController controller;

    @Test
    public void testStartState() {
        when(controller.start(any(Principal.class), isNull()))
        .thenReturn(dataHelper.gameStartResponseEntity());

        Principal principal = dataHelper.getPrincipal();

        ResponseEntity<GameResponse> response = controller.start(principal, null);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(dataHelper.gameStartResponse(), response.getBody());
        verify(controller, times(1)).start(principal, null);
    }

    @Test
    public void testPlayState() {
        when(controller.start(any(Principal.class), isNull()))
                .thenReturn(dataHelper.gamePlayResponseEntity());

        Principal principal = dataHelper.getPrincipal();

        ResponseEntity<GameResponse> response = controller.start(principal, null);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(dataHelper.gamePlayResponse(), response.getBody());
        verify(controller, times(1)).start(principal, null);
    }

    @Test
    public void testWaitingState() {
        when(controller.start(any(Principal.class), isNull()))
                .thenReturn(dataHelper.gameWaitingResponseEntity());

        Principal principal = dataHelper.getPrincipal();

        ResponseEntity<GameResponse> response = controller.start(principal, null);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(dataHelper.gameWaitingResponse(), response.getBody());
        verify(controller, times(1)).start(principal, null);
    }

    @Test
    public void testDisconnectState() {
        when(controller.start(any(Principal.class), isNull()))
                .thenReturn(dataHelper.gameDisconnectResponseEntity());

        Principal principal = dataHelper.getPrincipal();

        ResponseEntity<GameResponse> response = controller.start(principal, null);
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertEquals(dataHelper.gameDisconnectResponse(), response.getBody());
        verify(controller, times(1)).start(principal, null);
    }

    @Test
    public void testOverState() {
        when(controller.start(any(Principal.class), isNull()))
                .thenReturn(dataHelper.gameOverResponseEntity());

        Principal principal = dataHelper.getPrincipal();

        ResponseEntity<GameResponse> response = controller.start(principal, null);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(dataHelper.gameOverResponse(), response.getBody());
        verify(controller, times(1)).start(principal, null);
    }

    @Test
    public void testNumber() {
        doNothing().when(controller).number(any(GameRequest.class), any(Principal.class));
        Principal principal = dataHelper.getPrincipal();
        controller.number(dataHelper.gameRequest(), principal);
        verify(controller, times(1)).number(dataHelper.gameRequest(), principal);
    }

    @Test
    public void testPlay() {
        doNothing().when(controller).play(any(GameRequest.class), any(Principal.class));
        Principal principal = dataHelper.getPrincipal();
        controller.play(dataHelper.gameRequest(), principal);
        verify(controller, times(1)).play(dataHelper.gameRequest(), principal);
    }
}
