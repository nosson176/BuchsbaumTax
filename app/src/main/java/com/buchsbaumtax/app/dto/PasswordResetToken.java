package com.buchsbaumtax.app.dto;

import java.time.OffsetDateTime;

public class PasswordResetToken {
    private Long id;
    private String email;
    private String token;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private boolean used;

    public PasswordResetToken() {}

    public PasswordResetToken(String email, String token, OffsetDateTime expiresAt) {
        this.email = email;
        this.token = token;
        this.createdAt = OffsetDateTime.now();
        this.expiresAt = expiresAt;
        this.used = false;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
