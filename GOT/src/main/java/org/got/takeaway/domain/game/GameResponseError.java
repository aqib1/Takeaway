package org.got.takeaway.domain.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.got.takeaway.domain.base.Base;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameResponseError extends Base {
    private String createdAt;
    private String detailedMessage;
    private int errorCode;
    private String exceptionName;
    private String errorMessage;
}
