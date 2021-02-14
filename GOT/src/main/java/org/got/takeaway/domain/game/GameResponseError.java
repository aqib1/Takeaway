package org.got.takeaway.domain.game;

import lombok.Builder;
import org.got.takeaway.domain.base.Base;

@Builder
public class GameResponseError extends Base {
    private String createdAt;
    private String detailedMessage;
    private int errorCode;
    private String exceptionName;
    private String errorMessage;
}
