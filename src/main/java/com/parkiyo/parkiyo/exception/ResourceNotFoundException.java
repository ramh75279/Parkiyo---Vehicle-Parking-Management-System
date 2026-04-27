package com.parkiyo.parkiyo.exception;
import lombok.Getter;

@Getter

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: constructor with message + cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}