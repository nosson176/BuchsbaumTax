package com.sifradigital.framework.validation;

public class ValidationResult {

    private final boolean valid;
    private final String message;

    ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = !valid ? message : null;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
