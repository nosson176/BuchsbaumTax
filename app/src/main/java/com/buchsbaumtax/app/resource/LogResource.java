package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.LogCRUD;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Authenticated
@Path("/logs")
public class LogResource {

    @POST
    public Log createLog(Log log) {
        return new LogCRUD().create(log);
    }

    @PUT
    @Path("/{logId}")
    public Log updateLog(@PathParam("logId") int logId, Log log) {
        return new LogCRUD().update(logId, log);
    }
}
