package com.nautilus.exception;

public class GenericRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public GenericRuntimeException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
