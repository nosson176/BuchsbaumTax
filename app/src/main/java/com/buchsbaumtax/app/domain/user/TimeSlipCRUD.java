package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.config.Role;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TimeSlipDAO;
import com.buchsbaumtax.core.model.TimeSlip;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class TimeSlipCRUD {
    public List<TimeSlip> getUserTimeSlips(User user) {
        return Database.dao(TimeSlipDAO.class).getForUser(user.getId());
    }

    public List<TimeSlip> getAllTimeSlips() {
        return Database.dao(TimeSlipDAO.class).getAll();
    }

    public TimeSlip create(User user, TimeSlip timeSlip) {
        if (!user.getUserType().equals(Role.ADMIN) || user.getId() != timeSlip.getUserId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        int id = Database.dao(TimeSlipDAO.class).create(timeSlip);
        return Database.dao(TimeSlipDAO.class).get(id);
    }

    public TimeSlip update(User user, int timeSlipId, TimeSlip timeSlip) {
        if (!user.getUserType().equals(Role.ADMIN) || user.getId() != timeSlip.getUserId() || timeSlipId != timeSlip.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Database.dao(TimeSlipDAO.class).update(timeSlip);
        return timeSlip;
    }

    public BaseResponse delete(User user, int timeSlipId) {
        TimeSlip timeSlip = Database.dao(TimeSlipDAO.class).get(timeSlipId);
        if (!user.getUserType().equals(Role.ADMIN) || user.getId() != timeSlip.getUserId() || timeSlipId != timeSlip.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Database.dao(TimeSlipDAO.class).delete(timeSlipId);
        return new BaseResponse(true);
    }
}
