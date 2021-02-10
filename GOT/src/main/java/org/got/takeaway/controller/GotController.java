package org.got.takeaway.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

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

    @MessageMapping("/start")
    @SendToUser("/got/sender/state")
    public String test(String n) {
        return "Starting Game";
    }

}
