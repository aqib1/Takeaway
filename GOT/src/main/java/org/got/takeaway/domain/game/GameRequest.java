package org.got.takeaway.domain.game;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.got.takeaway.domain.base.Base;

@Builder
@Getter
@ToString
public class GameRequest extends Base {
    private int number;
    private int moveAttr;
}
