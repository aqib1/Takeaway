package org.got.takeaway.utils;

import org.got.takeaway.domain.game.GameResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface Helper {
   static ResponseEntity<GameResponseError> handleResponseErrors(Throwable e) {
       String error = Optional.of(e.getMessage()).orElse(e.getClass().getName())
               + " [Internal server exception!]";
       GameResponseError errorResponse = GameResponseError.builder().createdAt(LocalDateTime.now().toString())
               .detailedMessage(error).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
               .exceptionName(RuntimeException.class.getName()).errorMessage(e.getMessage()).build();
       return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
