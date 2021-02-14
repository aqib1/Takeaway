package org.got.takeaway.service;

import org.got.takeaway.domain.base.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    void notifyPlayer(String name, ResponseEntity<? extends BaseResponse> response);
}
