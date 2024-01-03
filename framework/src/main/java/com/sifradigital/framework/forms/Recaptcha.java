package com.sifradigital.framework.forms;

import com.google.gson.Gson;
import com.sifradigital.framework.json.GsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class Recaptcha {

    private static final Logger Log = LoggerFactory.getLogger(Recaptcha.class);

    private final String secretKey;
    private String version = "v3";
    private double threshold = 0.5;

    public Recaptcha(String secretKey) {
        this.secretKey = secretKey;
    }

    public Recaptcha(String secretKey, String version, double threshold) {
        this.secretKey = secretKey;
        this.version = version;
        this.threshold = threshold;
    }

    public boolean validate(String token) {
        try {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("secret", secretKey);
            formData.add("response", token);
            javax.ws.rs.client.Client client = ClientBuilder.newClient(new ClientConfig().register(new GsonProvider()));
            RecaptchaResponse response = client.target("https://www.google.com/recaptcha/api/siteverify")
                    .request(MediaType.APPLICATION_JSON_TYPE).post(Entity.form(formData), RecaptchaResponse.class);
            Log.debug("Recaptcha response: {}", new Gson().toJson(response));

            if (version.equals("v3")) {
                return response.getScore() >= threshold;
            }
            else {
                return response.isSuccess();
            }
        }
        catch (Exception e) {
            Log.error("Recaptcha validation error", e);
            return false;
        }
    }

    private static class RecaptchaResponse {

        private boolean success;
        private double score;

        boolean isSuccess() {
            return success;
        }

        public double getScore() {
            return score;
        }
    }
}
