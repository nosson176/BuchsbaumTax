package com.sifradigital.framework.client;

public class OauthRequest {

    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    private final String clientId;
    private final String clientSecret;
    private final String grantType;

    public OauthRequest(String clientId, String clientSecret, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }
}
