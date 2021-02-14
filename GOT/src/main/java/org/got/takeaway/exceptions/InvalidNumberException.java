package org.got.takeaway.exceptions;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException() {
        super();
    }

    public InvalidNumberException(String message) {
        super(message);
    }

    public InvalidNumberException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
