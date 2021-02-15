package org.got.takeaway.unit.configuration;

import org.got.takeaway.configuration.AuthChannelInterceptor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthChannelInterceptorUnitTest {

    @Mock
    private AuthChannelInterceptor interceptor;

    @Test
    public void testPreSend() {
        when(interceptor.preSend(isNull(), isNull())).thenReturn(null);
         interceptor.preSend(null, null);
         verify(interceptor, times(1)).preSend(null, null);
    }
}
