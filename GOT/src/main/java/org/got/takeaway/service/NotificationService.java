package org.got.takeaway.service;

import org.got.takeaway.domain.base.Base;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    void notifyPlayer(String name, ResponseEntity<? extends Base> response);
}
