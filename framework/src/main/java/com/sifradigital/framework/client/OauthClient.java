package com.sifradigital.framework.client;

import com.sifradigital.framework.json.GsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import java.util.Base64;

public class OauthClient {

    private static final Logger Log = LoggerFactory.getLogger(OauthClient.class);

    private final String url;

    private final Client client = ClientBuilder.newClient(new ClientConfig().register(new GsonProvider()));

    public OauthClient(String url) {
        this.url = url;
    }

    public OauthResponse grant(OauthRequest request) {
        Form form = new Form("grant_type", request.getGrantType());

        String credentials = request.getClientId() + ":" + request.getClientSecret();
        byte[] bytes = credentials.getBytes();
        String encoded = Base64.getEncoder().encodeToString(bytes);

        try {
            return client.target(url)
                    .request()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + encoded)
                    .post(Entity.form(form), OauthResponse.class);
        }
        catch (WebApplicationException e) {
            Log.debug("Client response error code {}", e.getResponse().getStatus());
            Log.debug("Client response error: {}", e.getResponse().readEntity(String.class));
            return null;
        }
    }
}
