package com.nautilus.exception;

public class ControllerException extends Exception {
    private final int status;
    private ErrorResponse errorResponse;

    public ControllerException(int status, ErrorResponse errorResponse) {
        this.status = status;
        this.errorResponse = errorResponse;
    }

    public int getStatus() {
        return this.status;
    }

    public ErrorResponse getErrorResponse() {
        return this.errorResponse;
    }
}
