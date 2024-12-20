package com.buchsbaumtax.app.job;

public class TokenValidationResult {
    private boolean valid;
    private PasswordResetToken token;

    public TokenValidationResult(boolean valid, PasswordResetToken token) {
        this.valid = valid;
        this.token = token;
    }

    // Getters
    public boolean isValid() { return valid; }
    public PasswordResetToken getToken() { return token; }


    @Override
    public String toString() {
        return "TokenValidationResult{" +
                "valid=" + valid +
                ", token=" + token +
                '}';
    }
}
