package com.job_tracker.create_exception;

public class SamePasswordException extends RuntimeException {
  public SamePasswordException(String message) {
    super(message);
  }
}
