package com.job_tracker.CreateException;

public class InvalidApplicationStatusTransition extends RuntimeException {
    public InvalidApplicationStatusTransition(String message) {
        super(message);
    }
}
