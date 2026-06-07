package com.job_tracker.create_exception;

public class InvalidDueAtExeption extends RuntimeException {
  public InvalidDueAtExeption(String message) {
    super(message);
  }
}
