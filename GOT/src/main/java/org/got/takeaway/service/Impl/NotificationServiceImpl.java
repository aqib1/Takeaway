package org.got.takeaway.service.Impl;

import org.got.takeaway.domain.base.BaseResponse;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static org.got.takeaway.utils.AppConst.UPDATE_QUEUE_URL;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyPlayer(String name, ResponseEntity<? extends BaseResponse> response) {
            messagingTemplate.convertAndSendToUser(name, UPDATE_QUEUE_URL, response);
    }
}
