package com.assignment.gerimedica.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    private final HttpStatus status;
    public HttpStatus getStatus() {
        return this.status;
    }

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
