package com.mito.sectask.exceptions.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Unauthorized resource access");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
