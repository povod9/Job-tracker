package com.job_tracker.create_exception;

public class InvalidApplicationStatusTransition extends RuntimeException {
    public InvalidApplicationStatusTransition(String message) {
        super(message);
    }
}
