package org.got.takeaway.exceptions;

public class OpponentNotFoundException extends RuntimeException {
    public OpponentNotFoundException() {
    }

    public OpponentNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public OpponentNotFoundException(String message) {
        super(message);
    }
}
