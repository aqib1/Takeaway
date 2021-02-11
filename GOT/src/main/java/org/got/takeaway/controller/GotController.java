package org.got.takeaway.controller;

import org.got.takeaway.domain.game.GameResponse;
import org.got.takeaway.service.Impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static org.got.takeaway.utils.AppConst.USERNAME;

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

    @MessageMapping("/start")
    @SendToUser("/queue/advise")
    public GameResponse start(Principal principal, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put(USERNAME, principal.getName());
        return gameService.start(principal.getName());
    }

}
