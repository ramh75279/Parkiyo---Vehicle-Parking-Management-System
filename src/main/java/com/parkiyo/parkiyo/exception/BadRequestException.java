package com.parkiyo.parkiyo.exception;
import lombok.Getter;

@Getter

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    // Optional: constructor with message + cause
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}