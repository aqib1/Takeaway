package org.got.takeaway.domain.player;

import lombok.*;
import org.got.takeaway.domain.base.Base;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "opponent")
public class Player extends Base {
    private String name;
    private Player opponent;
    private boolean primary;
    private PlayerStatus playerStatus;
}
