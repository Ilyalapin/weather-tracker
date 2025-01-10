package com.weather_tracker.commons.exception;

public class SessionNotInitializedException extends RuntimeException {
    public SessionNotInitializedException(String message) {
        super(message);
    }
}
