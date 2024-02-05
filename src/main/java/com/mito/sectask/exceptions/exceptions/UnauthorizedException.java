package com.mito.sectask.exceptions.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized resource access");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
