package com.parkiyo.parkiyo.exception;
import lombok.Getter;

@Getter

public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }

    // Optional: constructor with message + cause
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}