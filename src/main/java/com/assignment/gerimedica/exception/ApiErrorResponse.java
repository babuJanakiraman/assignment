package com.assignment.gerimedica.exception;

import lombok.Data;

@Data
public class ApiErrorResponse {

    private String message;
    private int statusCode;
    public ApiErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
