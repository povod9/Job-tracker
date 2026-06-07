package com.job_tracker.create_exception;

public class InvalidDueAtException extends RuntimeException {
    public InvalidDueAtException(String message) {
        super(message);
    }
}
