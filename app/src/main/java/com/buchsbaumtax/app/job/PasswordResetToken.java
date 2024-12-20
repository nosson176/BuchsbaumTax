package com.buchsbaumtax.app.job;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class PasswordResetToken {

    private String email;
    private String token;
    private OffsetDateTime created;;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String email, String token, OffsetDateTime  expiryDate) {
        this.email = email;
        this.token = token;
        this.created = expiryDate;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public OffsetDateTime  getExpiryDate() {
          return created;
    }

    @Override
    public String toString() {
        return "PasswordResetToken{" +

                ", email='" + email + '\'' +
                ", token=" + token +
                ", expiryDate=" + created +
                '}';
    }
}
