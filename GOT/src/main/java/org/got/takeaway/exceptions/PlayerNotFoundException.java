package org.got.takeaway.exceptions;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException() {
    }

    public PlayerNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
