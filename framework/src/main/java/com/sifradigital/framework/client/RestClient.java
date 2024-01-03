package com.sifradigital.framework.client;

import com.sifradigital.framework.json.GsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestClient {

    private static final Logger Log = LoggerFactory.getLogger(RestClient.class);

    private final Client client = ClientBuilder.newClient(new ClientConfig().register(new GsonProvider()));

    private final String baseUrl;
    private String authHeader;
    private boolean logRequests;
    private boolean logErrors;

    public RestClient(String baseUrl) {
        this(baseUrl, null, false, false);
    }

    public RestClient(String baseUrl, String authHeader) {
        this(baseUrl, authHeader, false, false);
    }

    public RestClient(String baseUrl, String authHeader, boolean logRequests, boolean logErrors) {
        this.baseUrl = baseUrl;
        this.authHeader = authHeader;
        this.logRequests = logRequests;
        this.logErrors = logErrors;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public void setLogRequests(boolean logRequests) {
        this.logRequests = logRequests;
    }

    public void setLogErrors(boolean logErrors) {
        this.logErrors = logErrors;
    }

    public void setBearerToken(String token) {
        this.authHeader = "Bearer " + token;
    }

    public void setBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        byte[] bytes = credentials.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        this.authHeader = "Basic " + encoded;
    }

    public <T> T get(String path, Class<T> responseType) {
        return request("GET", path, null, responseType);
    }

    public void post(String path, Object body) {
        request("POST", path, body, Void.class);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return request("POST", path, body, responseType);
    }

    public void put(String path, Object body) {
        request("PUT", path, body, Void.class);
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        return request("PUT", path, body, responseType);
    }

    public void delete(String path){
        request("DELETE", path, null, Void.class);
    }

    public <T> T delete(String path, Class<T> responseType){
        return request("DELETE", path, null, responseType);
    }

    private <T> T request(String method, String path, Object body, Class<T> responseType) {
        String url = baseUrl + "/" + path;

        if (logRequests) {
            Log.debug("Client {} request to {}", method, url);
        }

        try {
            Entity<Object> entity = body == null ? null : Entity.entity(body, MediaType.APPLICATION_JSON);
            Invocation.Builder builder = client.target(url).request().header("Accept", "application/json");

            if (authHeader != null) {
                builder = builder.header("Authorization", authHeader);
            }

            return builder.method(method, entity, responseType);
        }
        catch (WebApplicationException e) {
            if (logErrors) {
                Log.debug("Client response error code {}", e.getResponse().getStatus());
                Log.debug("Client response error: {}", e.getResponse().readEntity(String.class));
            }
            throw e;
        }
    }
}
