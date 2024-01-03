package com.sifradigital.framework.error;

public class ErrorResponse {

    private final int code;
    private final String message;
    private final String details;

    public ErrorResponse(String message) {
        this(500, message);
    }

    public ErrorResponse(int code, String message) {
        this(code, message, null);
    }

    public ErrorResponse(int code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
