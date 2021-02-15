package org.got.takeaway.integeration.controller;

import org.got.takeaway.domain.base.ResponseEntity;
import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameStatus;
import org.got.takeaway.exceptions.PlayerNotFoundException;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.got.takeaway.service.Impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.got.takeaway.utility.TestConst.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GotControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private GameServiceImpl gameService;

    @MockBean
    private PlayerRepositoryImpl playerRepository;

    private WebSocketStompClient stompClient;

    private String wsUrl;

    private CompletableFuture<Object> completableFuture;


    @BeforeEach
    public void setup() {
        wsUrl = String.format(SOCKJS_URL, port);
        stompClient = new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        completableFuture = new CompletableFuture<>();
    }


    @Test
    public void nonSpecifiedUser() throws Exception {
        // when / then
        assertThatExceptionOfType(ExecutionException.class)
                .isThrownBy(() -> {
                    stompClient.connect(wsUrl, new StompSessionHandlerAdapter() {
                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            assertThat(headers.getFirst("message")).isEqualTo("username is required to establish a connection");
                        }
                    }).get();
                });
    }


    @Test
    public void duplicateUser() {
        // given
        var stompHeaders = new StompHeaders();
        stompHeaders.add("username", PLAYER_1);

        given(playerRepository.isExists(anyString()))
                .willReturn(true);

        assertThatExceptionOfType(ExecutionException.class)
                .isThrownBy(() -> {
                    stompClient.connect(wsUrl, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            assertThat(headers.getFirst("message")).isEqualTo("Player with username already connected!!");
                        }
                    }).get();
                });

        verify(playerRepository).isExists(anyString());
    }

    @Test
    public void startTest() throws Exception {

        // given
        given(gameService.start(anyString()))
                .willReturn(GameResponse.builder().status(GameStatus.WAITING).build());

        // when
        StompSession stompSession = createSession(new MappingJackson2MessageConverter());

        stompSession.subscribe(USER_UPDATE_QUEUE_URL, new TestStompFrameHandler(ResponseEntity.class));
        stompSession.send(START_REQUEST_URL, null);

        // then
        var message = (ResponseEntity) completableFuture.get(3, SECONDS);
        var response = (LinkedHashMap) message.getBody();
        assertThat(response.get("status")).isEqualTo(GameStatus.WAITING.toString());
    }

    @Test
    public void errorEndpointTest() throws Exception {
        // given
        when(gameService.start(any())).thenThrow(new PlayerNotFoundException("No player currently available."));

        // when
        StompSession stompSession = createSession(new MappingJackson2MessageConverter());
        stompSession.subscribe(USER_UPDATE_ERROR_URL, new TestStompFrameHandler(ResponseEntity.class));
        stompSession.send(START_REQUEST_URL, null);

        // then
        ResponseEntity errorMessage = (ResponseEntity) completableFuture.get(3, SECONDS);
        var response = (LinkedHashMap) errorMessage.getBody();
        assertEquals("No player currently available. [Internal server exception!]", response.get("detailedMessage"));
        assertEquals(500, response.get("errorCode"));
    }


    @Test
    public void numberEndpointTest() throws Exception {
        // given
        var stompSession = createSession(new MappingJackson2MessageConverter());

        // when / then
        stompSession.setAutoReceipt(true);
        stompSession.send(SCORE_REQUEST_URL, GameResponse.builder().number(10).build())
                .addReceiptTask(() -> verify(gameService).number(eq(10), eq("test")));
    }

    @Test
    public void playEndpointTest() throws Exception {
        // given
        var stompSession = createSession(new MappingJackson2MessageConverter());
        var gameInstruction = GameRequest.builder().number(12).moveAttr(0).build();

        // when / then
        stompSession.setAutoReceipt(true);
        stompSession.send(PLAY_URL, gameInstruction)
                .addReceiptTask(() -> verify(gameService).play(eq(gameInstruction), eq("Xavier")));
    }

    private StompSession createSession(MessageConverter messageConverter) throws Exception {
        var stompHeaders = new StompHeaders();
        stompHeaders.add("username", PLAYER_1);

        stompClient.setMessageConverter(messageConverter);
        return stompClient.connect(wsUrl, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                exception.printStackTrace();
            }
        }).get(1, SECONDS);
    }


    class TestStompFrameHandler implements StompFrameHandler {

        private final Class<?> aClass;

        public TestStompFrameHandler(Class aClass) {
            this.aClass = aClass;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return aClass;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            completableFuture.complete(payload);
        }
    }

}
