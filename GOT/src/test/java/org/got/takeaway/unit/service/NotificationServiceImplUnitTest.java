package org.got.takeaway.unit.service;

import org.got.takeaway.service.Impl.NotificationServiceImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationServiceImplUnitTest {

    private final DataHelper dataHelper = DataHelper.getInstance();
    @Mock
    private NotificationServiceImpl notificationService;

    @Test
    public void testNotifyPlayer() {
        doNothing().when(notificationService).notifyPlayer(anyString(), any(ResponseEntity.class));
        notificationService.notifyPlayer("any", dataHelper.gameStartResponseEntity());
        verify(notificationService, times(1)).notifyPlayer("any", dataHelper.gameStartResponseEntity());
    }
}
