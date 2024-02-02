package com.buchsbaumtax.app.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/health")
public class HealthResource {

    @GET
    public Response checkHealth() {
        return Response.ok("Server is running. Health check passed.").build();
    }
};
