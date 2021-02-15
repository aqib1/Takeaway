package org.got.takeaway.integeration.service;

import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.got.takeaway.service.Impl.NotificationServiceImpl;
import org.got.takeaway.service.Impl.PlayerServiceImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceIT {

    private final DataHelper helper = DataHelper.getInstance();

    @Mock
    private NotificationServiceImpl notificationService;

    @Mock
    private PlayerRepositoryImpl playerRepository;

    @InjectMocks
    private PlayerServiceImpl service;

    @Captor
    private ArgumentCaptor<ResponseEntity<GameResponse>> messageCaptor;


    @Test
    public void savePlayer() {
        // given
        var player = helper.player("Peter");

        // when
        service.save(player);

        // then
        verify(playerRepository).save(player);
    }


    @Test
    public void removePlayerAndUpdateOpponent() {
        // given
        var player = helper.player("Viktoria");
        var opponent = helper.player("Anna");
        player.setOpponent(opponent);
        opponent.setOpponent(player);
        player.setPlayerStatus(PlayerStatus.PAIRED);
        opponent.setPlayerStatus(PlayerStatus.PAIRED);

        given(playerRepository.findByName(BDDMockito.anyString()))
                .willReturn(player);

        assertThat(opponent.getPlayerStatus()).isEqualTo(PlayerStatus.PAIRED);

        // when
        service.delete("Viktoria");

        // then
        assertThat(opponent.getOpponent()).isNull();
        assertThat(opponent.getPlayerStatus()).isEqualTo(PlayerStatus.AVAILABLE);

        verify(playerRepository).delete(eq("Viktoria"));
        verify(playerRepository).save(eq(opponent));

        verify(notificationService).notifyPlayer(eq("Anna"), messageCaptor.capture());

        var message = messageCaptor.getValue();
        assertThat(message.getBody().getStatus()).isEqualTo(GameStatus.DISCONNECT);
        assertThat(message.getBody().getMessage()).isEqualTo("Viktoria disconnected from game");
    }
}
