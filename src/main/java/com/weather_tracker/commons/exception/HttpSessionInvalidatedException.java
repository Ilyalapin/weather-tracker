package com.weather_tracker.commons.exception;

public class HttpSessionInvalidatedException extends RuntimeException {
    public HttpSessionInvalidatedException(String message) {
        super(message);
    }
}
