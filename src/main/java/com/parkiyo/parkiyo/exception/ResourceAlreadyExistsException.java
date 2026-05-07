package com.parkiyo.parkiyo.exception;
import lombok.Getter;

@Getter

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    // Optional: constructor with message + cause
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}