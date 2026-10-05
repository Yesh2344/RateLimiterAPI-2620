package com.example.ratelimiter.exception;

/**
 * Exception thrown when a client exceeds the allowed request rate.
 */
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}