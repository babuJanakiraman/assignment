package com.assignment.gerimedica.exception;

import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public RecordNotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }}