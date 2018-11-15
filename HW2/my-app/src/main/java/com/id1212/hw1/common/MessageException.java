package com.id1212.hw1.common;

/**
 * Thrown when the expected message could not be received.
 */
public class MessageException extends RuntimeException {
    public MessageException(String msg) {
        super(msg);
    }
    
    public MessageException(Throwable rootCause) {
        super(rootCause);
    }
}