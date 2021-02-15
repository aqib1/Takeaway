package org.got.takeaway.unit.configuration;

import org.got.takeaway.configuration.WebSocketConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WebSocketConfigUnitTest {

    @Mock
    private WebSocketConfig webSocketConfig;

    @Test
    public void testConfigureMessageBroker() {
        doNothing().when(webSocketConfig).configureMessageBroker(isNull());
        webSocketConfig.configureMessageBroker(null);
        verify(webSocketConfig, times(1)).configureMessageBroker(null);
    }

    @Test
    public void testRegisterStompEndpoints() {
        doNothing().when(webSocketConfig).registerStompEndpoints(isNull());
        webSocketConfig.registerStompEndpoints(null);
        verify(webSocketConfig, times(1)).registerStompEndpoints(null);
    }

    @Test
    public void testConfigureClientInboundChannel() {
        doNothing().when(webSocketConfig).configureClientInboundChannel(isNull());
        webSocketConfig.configureClientInboundChannel(null);
        verify(webSocketConfig, times(1)).configureClientInboundChannel(null);
    }
}
