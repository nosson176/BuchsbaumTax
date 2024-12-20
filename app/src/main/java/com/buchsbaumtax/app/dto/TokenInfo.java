package com.buchsbaumtax.app.dto;

public class TokenInfo {
    private String token;
    private String password;

    public TokenInfo(String token, String password) {
        this.token = token;
        this.password = password;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                ", token=" + token +
                ", password=" + password +
                '}';
    }
}


