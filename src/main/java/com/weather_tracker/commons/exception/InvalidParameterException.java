package com.weather_tracker.commons.exception;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String mesage) {
        super(mesage);
    }
}
