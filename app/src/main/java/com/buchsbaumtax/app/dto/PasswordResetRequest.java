package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.app.dto.TokenInfo;


public class PasswordResetRequest {

    private TokenInfo token;

    // Getters and setters
    public TokenInfo getToken() {
        return token;
    }

    public void setToken(TokenInfo token) {
        this.token = token;
    }

    public String getPassword() {
        return token.getPassword(); // Access password via token object
    }

}