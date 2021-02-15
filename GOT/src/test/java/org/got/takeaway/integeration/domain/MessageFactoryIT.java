package org.got.takeaway.integeration.domain;

import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.domain.message.MessageFactory;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageFactoryIT {

    private final DataHelper helper = DataHelper.getInstance();
    private final MessageFactory factory = MessageFactory.getInstance();

    @Test
    public void waitingResponse() {
        // given / when
        var message = factory.waitingResponse();

        // then
        assertThat(message.getStatus()).isEqualTo(GameStatus.WAITING);
        assertThat(message.getOpponent()).isNull();
        assertThat(message.isPrimary()).isFalse();
        assertThat(message.getMessage()).isEqualTo("Waiting for player");
    }


    @Test
    public void playResponse() {
        // given / when
        var message = factory.playResponse(40, helper.player("Test"));

        // then
        assertThat(message.getStatus()).isEqualTo(GameStatus.PLAY);
        assertThat(message.getNumber()).isEqualTo(40);
    }

    @Test
    public void gameOverResponse() {
        //  given / when
        var message = factory.gameOverResponse(true);

        // then
        assertThat(message.getStatus()).isEqualTo(GameStatus.OVER);
        assertThat(message.isWin()).isTrue();
    }

    @Test
    public void startResponse() {
        // given
        var player = helper.player("t1");
        player.setPrimary(true);
        var opponent = helper.player("t2");

        player.setOpponent(opponent);

        // when
        var message = factory.startingResponse(player.getOpponent());

        // then
        assertThat(message.getStatus()).isEqualTo(GameStatus.START);
        assertThat(message.getOpponent()).isEqualTo("t2");
        assertThat(message.isPrimary()).isFalse();
        assertThat(message.getMessage()).isEqualTo("[You're playing against: <span>t2</span>]");
    }

    @Test
    public void disconnectResponse() {
        // given / when
        var message = factory.disconnectResponse("Ali");

        // then
        assertThat(message.getStatus()).isEqualTo(GameStatus.DISCONNECT);
        assertThat(message.getMessage()).isEqualTo("Ali disconnected from game");
    }
}
