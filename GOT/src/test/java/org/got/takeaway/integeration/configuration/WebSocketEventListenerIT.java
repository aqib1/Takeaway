package org.got.takeaway.integeration.configuration;

import org.got.takeaway.configuration.WebSocketEventListener;
import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.got.takeaway.service.Impl.PlayerServiceImpl;
import org.got.takeaway.service.NotificationService;
import org.got.takeaway.utility.DataHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import static org.got.takeaway.utility.TestConst.PLAYER_1;
import static org.got.takeaway.utility.TestConst.PLAYER_2;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebSocketEventListenerIT {

    private final DataHelper helper = DataHelper.getInstance();

    @Mock
    private PlayerRepositoryImpl repository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SessionConnectedEvent sessionConnectedEvent;

    @Spy @InjectMocks
    private PlayerServiceImpl playerService;

    @Spy @InjectMocks
    private WebSocketEventListener listener;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveConnectedPlayer() {
        // given
        when(sessionConnectedEvent.getUser()).thenReturn(helper.getPrincipal());

        // when
        listener.handleWebSocketConnected(sessionConnectedEvent);

        // then
        verify(playerService, times(1)).save(helper.player(PLAYER_1));
    }

    @Test
    public void removeDisconnectedPlayer() {
        // given

        Player player1 = helper.player(PLAYER_1);
        player1.setPlayerStatus(PlayerStatus.PAIRED);

        Player player2 = helper.player(PLAYER_2);
        player2.setPlayerStatus(PlayerStatus.PAIRED);

        player1.setOpponent(player1);
        player2.setOpponent(player1);


        when(repository.findByName(PLAYER_1)).thenReturn(player1);
        doNothing().when(repository).delete(PLAYER_1);
        doNothing().when(notificationService).notifyPlayer(PLAYER_1, helper.gameDisconnectResponseEntity());

        // when
        listener.handleWebSocketDisconnected(helper.sessionDisconnectEvent());

        // then
        verify(playerService, times(1)).delete(PLAYER_1);
        verify(repository, times(1)).findByName(PLAYER_1);
        verify(repository, times(1)).delete(PLAYER_1);
        verify(notificationService, times(1)).notifyPlayer(PLAYER_1, helper.gameDisconnectResponseEntity());
    }

    @Test(expected = PlayerNotFoundException.class)
    public void removeDisconnectedPlayerNotFoundException() {
        when(repository.findByName(PLAYER_1)).thenReturn(null);
        listener.handleWebSocketDisconnected(helper.sessionDisconnectEvent());
    }

}
