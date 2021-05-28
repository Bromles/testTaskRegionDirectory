package com.bromles.testTaskForTelda.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private final String message;
    private final HttpStatus code;

    public ExceptionResponse(HttpStatus code) {
        this.code = code;
        this.message = code.name();
    }

    public ExceptionResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = code.name() + ": " + message;
    }
}
