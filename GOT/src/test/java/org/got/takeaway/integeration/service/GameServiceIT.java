package org.got.takeaway.integeration.service;

import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.exceptions.InvalidMoveException;
import org.got.takeaway.exceptions.OpponentNotFoundException;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.repositories.PlayerRepository;
import org.got.takeaway.service.Impl.GameServiceImpl;
import org.got.takeaway.service.Impl.NotificationServiceImpl;
import org.got.takeaway.service.Impl.PlayerServiceImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceIT {

    private final DataHelper helper = DataHelper.getInstance();

    @Mock
    private PlayerRepository playerRepository;

    @Spy
    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private GameServiceImpl gameService;

    @Captor
    private ArgumentCaptor<ResponseEntity<GameResponse>> messageCaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenPlayerWithNameIsNotFound_Then_ThrowException() {
        // given
        given(playerRepository.findByName(anyString()))
                .willReturn(null);

        // when / then
        assertThatExceptionOfType(PlayerNotFoundException.class)
                .isThrownBy(() -> gameService.start("Test"))
                .withMessage("[Test] player not exists in system!!");
    }

    @Test
    public void whenGameIsStarted_And_NoPlayerIsAvailable_Then_SendWaitMessage() {
        // given
        given(playerRepository.findByName(anyString()))
                .willReturn(helper.player("Test"));

        given(playerRepository.findAvailable(anyString()))
                .willReturn(null);

        // when
        var message = gameService.start("Test");

        assertThat(message.getStatus()).isEqualTo(GameStatus.WAITING);
        assertThat(message.getMessage()).isEqualTo("Waiting for player");
        verify(playerRepository, never()).save(any());
        verifyNoInteractions(notificationService);
    }


    @Test
    public void whenGameIsStarted_And_PlayerIsAvailable_Then_NotifyAvailablePlayer() {
        // given
        var player = helper.player("test1");
        var availablePlayer = helper.player("test2");

        given(playerRepository.findByName(anyString()))
                .willReturn(player);
        given(playerRepository.findAvailable(anyString()))
                .willReturn(availablePlayer);

        // when
        var message = gameService.start("test1");

        // then
        assertThat(player.isPrimary()).isTrue();
        assertThat(availablePlayer.isPrimary()).isFalse();
        assertThat(message.getStatus()).isEqualTo(GameStatus.START);
        assertThat(message.getOpponent()).isEqualTo("test2");
        assertThat(message.isPrimary()).isFalse();

        verify(notificationService).notifyPlayer(eq("test2"), messageCaptor.capture());

        var response = messageCaptor.getValue();
        assertThat(response.getBody().getStatus()).isEqualTo(GameStatus.START);
        assertThat(response.getBody().getOpponent()).isEqualTo("test1");
        assertThat(response.getBody().isPrimary()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("[You're playing against: <span>test1</span>]");
        verify(playerRepository).findAvailable(anyString());
    }


    @Test
    public void whenGameIsStarted_And_PlayerIsAlreadyPaired_Then_NotifyOpponent() {
        // given
        var player = helper.player("test1");
        var opponent = helper.player("test2");

        when(playerRepository.findAvailable("test1"))
                .thenReturn(opponent);
        when(playerRepository.findByName("test1"))
                .thenReturn(player);
        // when
        var message = gameService.start("test1");
        // then
        assertThat(message.getStatus()).isEqualTo(GameStatus.START);
        assertThat(message.getOpponent()).isEqualTo("test2");
        verify(playerRepository, never()).save(any());
        verify(notificationService).notifyPlayer(eq("test2"), any());
    }

    @Test(expected = PlayerNotFoundException.class)
    public void processRandomNumberFromPlayer_ShouldFailIfPlayerNotFound() {
        // given
        given(playerRepository.findByName(anyString()))
                .willReturn(null);

        // when / then
        gameService.number(42, "Tom");
    }

    @Test
    public void processRandomNumberFromPlayer_ShouldFailIfPlayerHasNotBeenPaired() {
        // given
        given(playerRepository.findByName(anyString()))
                .willReturn(helper.player("Jerry"));

        // when / then
        assertThatExceptionOfType(OpponentNotFoundException.class)
                .isThrownBy(() -> gameService.number(42, "Jerry"))
                .withMessage("Opponent not exists!!!");
    }

    @Test
    public void processRandomNumberFromPlayer_ShouldNotifyOpponent() {
        // given
        var randomNumber = 50;
        var player = helper.player("test1");
        player.setOpponent(helper.player("test2"));

        given(playerRepository.findByName(anyString()))
                .willReturn(player);

        // when
        gameService.number(randomNumber, player.getName());

        // then
        verify(notificationService).notifyPlayer(eq("test2"), messageCaptor.capture());

        var message = messageCaptor.getValue();
        assertThat(message.getBody().getStatus()).isEqualTo(GameStatus.PLAY);
        assertThat(message.getBody().getNumber()).isEqualTo(50);
    }

    @Test
    public void processPlayerMove_ShouldFailIfCombinationIsNotDivisibleByDivisor() {
        // given
        var instruction = GameRequest.builder().number(20).moveAttr(-1).build();
        var player = helper.player("test1");
        player.setOpponent(helper.player("test2"));


        given(playerRepository.findByName("test1"))
                .willReturn(player);
        // when / then
        assertThatExceptionOfType(InvalidMoveException.class)
                .isThrownBy(() -> gameService.play(instruction, "test1"))
                .withMessage("19 is not divisible by 3");
    }

    @Test
    public void processPlayerMove_ShouldFailIfPlayerNotFound() {
        // given
        var instruction = GameRequest.builder().number(41).moveAttr(1).build();

        given(playerRepository.findByName(anyString()))
                .willReturn(null);

        // when / then
        assertThatExceptionOfType(PlayerNotFoundException.class)
                .isThrownBy(() -> gameService.play(instruction, "test"));
    }


    @Test
    public void processPlayerMove_ShouldFailIfOpponentDoesNotExist() {
        // given
        given(playerRepository.findByName(anyString()))
                .willReturn(helper.player("Test1"));

        // when / then
        assertThatExceptionOfType(OpponentNotFoundException.class)
                .isThrownBy(() -> gameService.play(GameRequest.builder().number(12).moveAttr(0).build(), "Winter Soldier"));
    }

    @Test
    public void processPlayerMove_ShouldNotifyOpponentIfDivisionByThreeIsNotOne() {
        // given
        var instruction = GameRequest.builder().number(21).moveAttr(0).build();
        var player = helper.player("Test1");
        var opponent = helper.player("Test2");

        player.setOpponent(opponent);

        given(playerRepository.findByName(anyString()))
                .willReturn(player);

        // when
        gameService.play(instruction, "Test1");

        // then
        verify(notificationService).notifyPlayer(eq("Test2"), messageCaptor.capture());

        var message = messageCaptor.getValue();
        assertThat(message.getBody().getStatus()).isEqualTo(GameStatus.PLAY);
        assertThat(message.getBody().getMessage()).isEqualTo("Test1 player sent number 7");
    }


    @Test
    public void processPlayerMove_ShouldNotifyBothPlayersIsDivisionByThreeIsOne() {
        // given
        var instruction = GameRequest.builder().number(2).moveAttr(1).build();
        var player = helper.player("Test1");
        var opponent = helper.player("Test2");

        player.setOpponent(opponent);

        given(playerRepository.findByName(anyString()))
                .willReturn(player);

        // when
        gameService.play(instruction, "Test1");

        // then
        verify(notificationService).notifyPlayer(eq("Test1"), messageCaptor.capture());
        verify(notificationService).notifyPlayer(eq("Test2"), messageCaptor.capture());

        assertThat(messageCaptor.getAllValues()).extracting("status")
                .allMatch(type -> type.equals(HttpStatus.OK));

        assertThat(messageCaptor.getAllValues()).extracting("body").extracting("isWin").containsExactly(true, false);
        assertThat(messageCaptor.getAllValues()).extracting("body").extracting("status").containsExactly(GameStatus.OVER, GameStatus.OVER);
    }

}
