//package com.buchsbaumtax.app.dto;
//
//public class Token {
//
//    private String token;
//
//    public Token(String token){this.token = token;}
//}
package com.buchsbaumtax.app.dto;

public class Token {
    private String token;
    private String username;
    private long userId;

    public Token(String token, long userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }

    public long getUserId() {
        return userId;
    }
}
