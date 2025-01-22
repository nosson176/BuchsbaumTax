package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.LogCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.time.Instant;
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
        LocalDateTime threeYearsAgoStartOfYear = LocalDateTime.now()
                .minusYears(3) // Go back three years
                .withDayOfYear(1) // Set to the first day of the year
                .withHour(0).withMinute(0).withSecond(0).withNano(0); // Reset time to midnight

        Instant threeYearsAgoInstant = threeYearsAgoStartOfYear.toInstant(ZoneOffset.UTC);
        long threeYearsAgoStart = threeYearsAgoInstant.toEpochMilli();
        return Database.dao(LogDAO.class).getLogsBeforeLastThreeYears(clientId,threeYearsAgoStart);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        // Get current time and end of the day
        String currentTime = LocalDateTime.now().format(formatter);
        String endOfDay = LocalDateTime.now().withHour(23).withMinute(59).format(formatter);

        // Retrieve logs from DAO
        return Database.dao(LogDAO.class).getLogsBetweenTimes(currentTime, endOfDay);
    }
}
