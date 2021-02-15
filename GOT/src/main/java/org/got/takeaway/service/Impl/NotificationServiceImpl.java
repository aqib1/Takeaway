package org.got.takeaway.service.Impl;

import org.got.takeaway.domain.base.Base;
import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static org.got.takeaway.utils.AppConst.UPDATE_QUEUE_URL;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyPlayer(String name, ResponseEntity<? extends Base> response) {
            messagingTemplate.convertAndSendToUser(name, UPDATE_QUEUE_URL, response);
    }
}
