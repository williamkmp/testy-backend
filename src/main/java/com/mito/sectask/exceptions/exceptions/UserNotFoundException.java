package com.mito.sectask.exceptions.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Unauthorized resource access");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
