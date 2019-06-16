package com.nautilus.controller;

import com.nautilus.exception.ControllerException;
import com.nautilus.exception.ErrorCode;
import com.nautilus.exception.ErrorResponse;
import com.nautilus.exception.GenericRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

public abstract class AbstractController {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AbstractController() {
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        String message = null;
        Integer status = null;
        ErrorCode code = null;
        if (e instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN.value();
            code = ErrorCode.ACCESS_DENIED;
            message = "Доступ запрещен";
        } else if (e instanceof GenericRuntimeException) {
            code = ((GenericRuntimeException) e).getErrorCode();
            if (code != null) {
                switch (code) {
                    case UNKNOWN:
                        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
                        break;
                    case NOT_FOUND:
                        status = HttpStatus.NOT_FOUND.value();
                        break;
                    case BINDING_ERROR:
                        status = HttpStatus.BAD_REQUEST.value();
                        break;
                    case ACCESS_DENIED:
                        status = HttpStatus.FORBIDDEN.value();
                }
            }
        } else if (e instanceof ControllerException) {
            ControllerException ex = (ControllerException) e;
            if (ex.getErrorResponse() != null) {
                return ResponseEntity.status(ex.getStatus()).body(((ControllerException) e).getErrorResponse());
            }

            status = ex.getStatus();
        } else if (e instanceof HttpMessageNotReadableException && e.getCause() != null) {
            Throwable cause = e.getCause();
            if (cause.getCause() instanceof GenericRuntimeException) {
                e = (GenericRuntimeException) cause.getCause();
                code = ((GenericRuntimeException) e).getErrorCode();
            }
        }

        String details = e.getCause() != null ? e.getCause().getMessage() : null;
        if (message == null) {
            StringBuilder builder = new StringBuilder();
            exceptionToString(e, builder);
            message = builder.toString();
        }

        this.log.warn("Обработана ошибка", e);
        return ResponseEntity.status(status != null ? status : HttpStatus.BAD_REQUEST.value()).body(new ErrorResponse(code != null ? code : ErrorCode.UNKNOWN, message, details));
    }

    ControllerException buildException(BindingResult bindingResult) {
        return new ControllerException(HttpStatus.BAD_REQUEST.value(), new ErrorResponse(bindingResult.getFieldErrors().stream().map((i) -> new ErrorResponse.Error(ErrorCode.BINDING_ERROR, i.getDefaultMessage(), i.getField())).collect(Collectors.toList())));
    }

    private static void exceptionToString(Throwable ex, StringBuilder builder) {
        builder.append(ex.getMessage());
        builder.append(LINE_SEPARATOR);
        Throwable cause = ex.getCause();
        if (cause != null) {
            exceptionToString(cause, builder);
        }

    }
}