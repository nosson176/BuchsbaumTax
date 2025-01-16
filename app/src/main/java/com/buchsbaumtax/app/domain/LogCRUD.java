package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.resource.WorkTimesResource;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class LogCRUD {
    private static final Logger logger = LoggerFactory.getLogger(LogCRUD.class);

    public Log create(Log log) {
        validate(log);
        int logId = Database.dao(LogDAO.class).create(log);
        return Database.dao(LogDAO.class).get(logId);
    }

    public Log update(int logId, Log log) {
        validate(log);
        Log oldLog = Database.dao(LogDAO.class).get(logId);
        if (log.getId() != logId || oldLog == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(LogDAO.class).update(log);
        return Database.dao(LogDAO.class).get(logId);
    }

    public BaseResponse delete(int logId){
        try {

        Log log = Database.dao(LogDAO.class).get(logId);

        if(log == null){
            return new BaseResponse("Error", "Log not found", Response.Status.NOT_FOUND.getStatusCode());
        }
        // Proceed with deletion if found
        Database.dao(LogDAO.class).delete(logId);

        // Return success response
        return new BaseResponse("Success", "Log deleted successfully", Response.Status.OK.getStatusCode());
    } catch (Exception e) {
        // Log the error (optional)
        e.printStackTrace();
        // Return failure response if exception occurs
        return new BaseResponse("Error", "Failed to delete Log: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public List<Log> update(List<Log> logs) {
//        logger.info("update it run!!!: {}", logs);
        Database.dao(LogDAO.class).update(logs);
        return logs.stream().map(l -> Database.dao(LogDAO.class).get(l.getId())).collect(Collectors.toList());
    }

    public List<Log> saveOrUpdateLogs(List<Log> logs) {
//        logger.info("saveOrUpdateLogs it run!!!: {}", logs);

        for (Log log : logs) {
//            logger.info("current log!!!: {}", logExists(log));
            if (logExists(log)) {
                // Update the log if it exists
//                logger.info("exits it run!!!: {}", log);
                Database.dao(LogDAO.class).update(log);
            } else {
                // Insert the log if it doesn't exist
//                logger.info("no exits it run!!!: {}", log);
                Database.dao(LogDAO.class).create(log);
            }
        }

        // Return updated or inserted logs by fetching them from the database
        return logs.stream().map(l -> Database.dao(LogDAO.class).get(l.getId())).collect(Collectors.toList());
    }

    private boolean logExists(Log log) {
        if (log.getId() > 0) {
            Log existingLog = Database.dao(LogDAO.class).get(log.getId());
//            logger.info("logExists???: {}", existingLog);

            // Check if the log is actually null or a valid log object
            if (existingLog != null && existingLog.getId() == log.getId()) {
                return true;
            }
        }
        return false;
    }

    private void validate(Log log) {
        new Validator()
                .required(log.getClientId())
                .validateAndGuard();
    }
}
