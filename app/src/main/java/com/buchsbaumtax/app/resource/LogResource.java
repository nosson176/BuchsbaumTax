package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.LogCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
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
        // Define the date format for the query
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Get current time and end of the day
        String currentTime = LocalDateTime.now().format(formatter);
        String endOfDay = LocalDateTime.now().withHour(23).withMinute(59).format(formatter);

        // Retrieve logs from DAO
        return Database.dao(LogDAO.class).getLogsBetweenTimes(currentTime, endOfDay);
    }
}
