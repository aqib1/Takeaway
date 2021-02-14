package org.got.takeaway.domain.player;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "opponent")
public class Player {
    private String name;
    private Player opponent;
    private boolean primary;
    private PlayerStatus playerStatus;
}
