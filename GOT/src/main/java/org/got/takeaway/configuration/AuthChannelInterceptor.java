package org.got.takeaway.configuration;

import org.got.takeaway.exceptions.DuplicatePlayerException;
import org.got.takeaway.exceptions.RequestException;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import static java.util.Optional.ofNullable;
import static org.got.takeaway.utils.AppConst.USERNAME;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private PlayerRepositoryImpl repository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            ofNullable(accessor.getFirstNativeHeader(USERNAME))
                  .filter(name -> !StringUtils.isEmpty(name))
                  .ifPresentOrElse(name -> {
                      playerAlreadyExists(name);
                      accessor.setUser(() -> name);
                  }, () -> {
                        throw new RequestException("Name required to proceed with the game!!!");
                  });
        }

        return message;
    }

    private void playerAlreadyExists(String name) {
        if(repository.isExists(name)) {
            throw new DuplicatePlayerException("Player already exists with same name");
        }
    }

}
