package org.got.takeaway.integeration.service;

import org.got.takeaway.service.Impl.NotificationServiceImpl;
import org.got.takeaway.service.NotificationService;
import org.got.takeaway.utility.DataHelper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.got.takeaway.utility.TestConst.UPDATE_QUEUE_URL;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceIT {

    private final DataHelper helper = DataHelper.getInstance();

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationServiceImpl service;


    @Test
    public void notifyPlayer_Should_SendMessageToPlayer() {
        // given
        var playerName = "Zoltan";
        var gameMessage = helper.gamePlayResponseEntity();


        // then
        service.notifyPlayer(playerName, gameMessage);

        // then
        verify(messagingTemplate).convertAndSendToUser(eq(playerName), eq(UPDATE_QUEUE_URL), eq(gameMessage));
    }
}
