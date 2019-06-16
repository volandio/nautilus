package com.nautilus.exception;

import lombok.Data;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse {
    private List<ErrorResponse.Error> errors;

    public ErrorResponse(ErrorCode code, String message, String details) {
        this(new ErrorResponse.Error(code, message, details));
    }

    public ErrorResponse(ErrorResponse.Error error) {
        this.errors = Collections.singletonList(error);
    }

    public ErrorResponse(List<ErrorResponse.Error> errors) {
        this.errors = errors;
    }

    @Getter
    public static class Error {
        private final ErrorCode code;
        private final String message;
        private final String details;

        public Error(ErrorCode code, String message, String details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }
}