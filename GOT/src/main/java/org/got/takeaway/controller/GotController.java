package org.got.takeaway.controller;

import org.got.takeaway.domain.game.GameRequest;
import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.domain.game.GameResponseError;
import org.got.takeaway.service.Impl.GameServiceImpl;
import org.got.takeaway.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static org.got.takeaway.utils.AppConst.*;

/**
 * @author Aqib
 * @version 0.0.1-RELEASE
 * @since 10/02/2021
 * <p>
 * This class is GOT(Game of three) controller class which is responsible of
 * routing all STOMP messages
 * </p>
 */
@Controller
public class GotController {

    @Autowired
    private GameServiceImpl gameService;

    @MessageMapping(START_REQUEST_URL)
    @SendToUser(UPDATE_QUEUE_URL)
    public ResponseEntity<GameResponse> start(Principal principal, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put(USERNAME, principal.getName());
        return ResponseEntity.ok(gameService.start(principal.getName()));
    }

    @MessageMapping(SCORE_REQUEST_URL)
    public void number(GameRequest request, Principal principal) {
        gameService.number(request.getNumber(), principal.getName());
    }

    @MessageMapping(PLAY_URL)
    public void play(GameRequest request, Principal principal) {
        gameService.play(request, principal.getName());
    }

    @MessageExceptionHandler
    @SendToUser(UPDATE_ERROR_URL)
    public ResponseEntity<GameResponseError> handleResponseErrors(Throwable e) {
        return Helper.handleResponseErrors(e);
    }

}
