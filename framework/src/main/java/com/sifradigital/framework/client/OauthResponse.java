package com.sifradigital.framework.client;

import com.google.gson.annotations.SerializedName;

public class OauthResponse {

    private String scope;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("app_id")
    private String appId;

    @SerializedName("expires_in")
    private String expiresIn;

    public String nonce;

    public String getScope() {
        return scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAppId() {
        return appId;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getNonce() {
        return nonce;
    }

}
