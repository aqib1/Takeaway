package org.got.takeaway.domain.game;

import lombok.Builder;
import org.got.takeaway.domain.base.BaseResponse;

@Builder
public class GameResponseError extends BaseResponse {
    private String createdAt;
    private String detailedMessage;
    private int errorCode;
    private String exceptionName;
    private String errorMessage;
}
