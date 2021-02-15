package org.got.takeaway.integeration.domain;

import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerIT {

    private final DataHelper helper = DataHelper.getInstance();

    @Test
    public void availableStatus() {
        // given / when
        var player = helper.player("T1");

        // then
        assertThat(player.getPlayerStatus()).isEqualTo(PlayerStatus.AVAILABLE);
        assertThat(player.getName()).isEqualTo("T1");
        assertThat(player.isPrimary()).isFalse();
        assertThat(player.getOpponent()).isNull();
    }


    @Test
    public void pairedStatus() {
        // given
        var player = helper.pairedPlayer("T1");
        var opponent = helper.pairedPlayer("T2");

        // when
        player.setOpponent(opponent);
        opponent.setOpponent(player);

        // then
        assertThat(player.getPlayerStatus()).isEqualTo(PlayerStatus.PAIRED);
        assertThat(player.getOpponent()).isEqualTo(opponent);
        assertThat(opponent.getPlayerStatus()).isEqualTo(PlayerStatus.PAIRED);
        assertThat(opponent.getOpponent()).isEqualTo(player);
    }
}
