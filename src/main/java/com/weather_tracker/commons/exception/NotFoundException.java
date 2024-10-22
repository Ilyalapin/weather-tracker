package com.weather_tracker.commons.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String mesage) {
        super(mesage);
    }
}
