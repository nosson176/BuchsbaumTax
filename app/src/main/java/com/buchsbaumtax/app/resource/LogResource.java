package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.LogCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/logs")
public class LogResource {

    @POST
    public Log createLog(Log log) {
        return new LogCRUD().create(log);
    }

    @GET
    public List<Log> getAllLogs() {
        return Database.dao(LogDAO.class).getAll();
    }

    @PUT
    public BaseResponse bulkUpdateLogs(List<Log> logs) {
        return new LogCRUD().bulkUpdate(logs);
    }

    @PUT
    @Path("/{logId}")
    public Log updateLog(@PathParam("logId") int logId, Log log) {
        return new LogCRUD().update(logId, log);
    }
}
