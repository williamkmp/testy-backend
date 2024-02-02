package com.mito.sectask.exceptions.exceptions;

public class MismatchedDataException extends RuntimeException {

    public MismatchedDataException() {
        super("Mismatched data exception");
    }

    public MismatchedDataException(String message) {
        super(message);
    }
}
