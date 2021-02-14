package org.got.takeaway.exceptions;

public class DuplicatePlayerException extends RuntimeException {
    public DuplicatePlayerException() {
    }

    public DuplicatePlayerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DuplicatePlayerException(String message) {
        super(message);
    }
}
