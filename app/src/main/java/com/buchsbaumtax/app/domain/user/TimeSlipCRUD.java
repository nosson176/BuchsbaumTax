package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.core.dao.TimeSlipDAO;
import com.buchsbaumtax.core.model.TimeSlip;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

public class TimeSlipCRUD {
    public List<TimeSlip> getUserTimeSlips(User user) {
        return Database.dao(TimeSlipDAO.class).getForUser(user.getId());
    }

    public List<TimeSlip> getAllTimeSlips() {
        return Database.dao(TimeSlipDAO.class).getAll();
    }

    public TimeSlip clockIn(User user) {
        if (getOpenShift(user) != null) {
            throw new WebApplicationException("User already has an active shift", Response.Status.BAD_REQUEST);
        }
        int id = Database.dao(TimeSlipDAO.class).create(user.getId());
        return Database.dao(TimeSlipDAO.class).get(id);
    }

    public TimeSlip clockOut(User user) {
        TimeSlip openShift = getOpenShift(user);
        if (openShift == null || openShift.getUserId() != user.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        openShift.setTimeOut(new Date());
        openShift.setUpdated(new Date());
        Database.dao(TimeSlipDAO.class).update(openShift);
        return openShift;
    }

    private TimeSlip getOpenShift(User user) {
        return Database.dao(TimeSlipDAO.class).getForUser(user.getId()).stream().filter(s -> s.getTimeOut() == null).findFirst().orElse(null);
    }
}
