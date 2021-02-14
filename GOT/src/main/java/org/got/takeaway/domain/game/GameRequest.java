package org.got.takeaway.domain.game;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class GameRequest {
    private int number;
    private int moveAttr;
}
