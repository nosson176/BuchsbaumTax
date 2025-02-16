package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.LogCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @GET
    @Path("/{clientId}")
    public List<Log> getAllLogsByClient(@PathParam("clientId") int clientId) {
        return Database.dao(LogDAO.class).getForClient(clientId);
    }

    @GET
    @Path("/restLogs/{clientId}")
    public List<Log> getARestLogsByClient(@PathParam("clientId") int clientId) {
        return Database.dao(LogDAO.class).getRemainingLogs(clientId);
    }

    @PUT
    public List<Log> updateLogs(List<Log> logs) {
        return new LogCRUD().saveOrUpdateLogs(logs);
    }

    @PUT
    @Path("/{logId}")
    public Log updateLog(@PathParam("logId") int logId, Log log) {
        return new LogCRUD().update(logId, log);
    }
    @DELETE
    @Path("/{logId}")
    public BaseResponse deleteLog(@PathParam("logId") int logId) {
        return new LogCRUD().delete(logId);
    }

    @GET
    @Path("/today")
    public List<Log> getTodayLogs() {
        // Get the current time and end of the day in Unix timestamp format (milliseconds)
        long currentTime = System.currentTimeMillis(); // Current time in milliseconds
        long endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // End of day in milliseconds

        // Retrieve logs from DAO
        return Database.dao(LogDAO.class).getLogsBetweenTimes(currentTime, endOfDay);
    }

}
