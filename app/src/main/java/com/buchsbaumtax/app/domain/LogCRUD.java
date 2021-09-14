package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class LogCRUD {
    public Log create(int clientId, Log log) {
        if (log.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        validate(log);
        int logId = Database.dao(LogDAO.class).create(log);
        return Database.dao(LogDAO.class).get(logId);
    }

    public Log update(int clientId, int logId, Log log) {
        validate(log);
        if (log.getId() != logId || log.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(LogDAO.class).update(log);
        return Database.dao(LogDAO.class).get(logId);
    }

    public BaseResponse delete(int logId) {
        Database.dao(LogDAO.class).delete(logId);
        return new BaseResponse(true);
    }

    private void validate(Log log) {
        new Validator()
                .required(log.getClientId())
                .after(log.getAlarmDate(), log.getLogDate())
                .validateAndGuard();
    }
}
