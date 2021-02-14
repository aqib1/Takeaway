package org.got.takeaway.exceptions;

public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException() {
        super();
    }

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
