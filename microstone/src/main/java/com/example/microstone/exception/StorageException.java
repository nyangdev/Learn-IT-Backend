package com.example.microstone.exception;

public class StorageException extends RuntimeException {
    public StorageException(String Message) {
        super(Message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
