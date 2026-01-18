package com.voluntree.backend.exception;

public class UnauthorizedActivityAccessException extends RuntimeException {
    public UnauthorizedActivityAccessException(String message) {
        super(message);
    }
}
