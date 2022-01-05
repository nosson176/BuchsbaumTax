package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class LogCRUD {
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

    public BaseResponse bulkUpdate(List<Log> logs) {
        Database.dao(LogDAO.class).bulkUpdate(logs);
        return new BaseResponse(true);
    }

    private void validate(Log log) {
        new Validator()
                .required(log.getClientId())
                .validateAndGuard();
    }
}
