package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.core.dao.WorkTimeDAO;
import com.buchsbaumtax.core.model.WorkTimes;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkTimesCRUD {
    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

    public WorkTimes create(WorkTimes workTimes) {
        logger.info("APP_MODE is set to: {}", workTimes.getDate());
        validate(workTimes);
        long now = System.currentTimeMillis(); // Current time in milliseconds
        workTimes.setStartTime(now);
        int workTimesId = Database.dao(WorkTimeDAO.class).create(workTimes);
        return Database.dao(WorkTimeDAO.class).getById(workTimesId);
    }

    public WorkTimes update(int workTimesId, WorkTimes workTimes) {
        validate(workTimes);

        logger.info("Request to update work times - ID: {}, Start Time: {}, End Time: {}, Sum Hours Work: {}",
                workTimesId, workTimes.getStartTime(), workTimes.getEndTime(), workTimes.getSumHoursWork());

        WorkTimes oldWorkTimes = Database.dao(WorkTimeDAO.class).getById(workTimesId);
        logger.info("Existing work times - ID: {}, Start Time: {}, End Time: {}, Sum Hours Work: {}",
                oldWorkTimes.getId(), oldWorkTimes.getStartTime(), oldWorkTimes.getEndTime(), oldWorkTimes.getSumHoursWork());

        if (workTimes.getId() != workTimesId || oldWorkTimes == null) {
            throw new WebApplicationException("Invalid work times ID or record not found.", Response.Status.BAD_REQUEST);
        }

        Database.dao(WorkTimeDAO.class).update(workTimes);

        WorkTimes updatedWorkTimes = Database.dao(WorkTimeDAO.class).getById(workTimesId);
        logger.info("Updated work times - ID: {}, Start Time: {}, End Time: {}, Sum Hours Work: {}",
                updatedWorkTimes.getId(), updatedWorkTimes.getStartTime(), updatedWorkTimes.getEndTime(), updatedWorkTimes.getSumHoursWork());

        return updatedWorkTimes;
    }



    private void validate(WorkTimes workTimes) {
        new Validator()
                .required(workTimes.getUserId(), "User ID is required")
                .validateAndGuard();

        if (workTimes.getEndTime() < workTimes.getStartTime()) {
            throw new WebApplicationException("Start time must be before end time", Response.Status.BAD_REQUEST);
        }
    }

    public WorkTimes clockIn(int userId, String username) {
        logger.info("APP_MODE is set to: {}", username);
        WorkTimes workTimes = new WorkTimes();
        workTimes.setUserId(userId);
        workTimes.setUsername(username);
        long now = System.currentTimeMillis(); // Current time in milliseconds
        workTimes.setStartTime(now);
        workTimes.setDate(now);
        int id = Database.dao(WorkTimeDAO.class).create(workTimes);
        return Database.dao(WorkTimeDAO.class).getById(id);
    }

    public WorkTimes clockOut(int userId, long date) {
        logger.info("Attempting to clock out userId: {} on date: {}", userId, date);

        // Find the most recent work entry for the user on the specified date with endTime = 0
        WorkTimes workTimes = Database.dao(WorkTimeDAO.class).getAllByUserIdAndDateWithNoEndTime(userId, date);

        if (workTimes == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        long now = System.currentTimeMillis(); // Current time in milliseconds
        workTimes.setEndTime(now);

        // Debug logs
        logger.info("Start time: {}", workTimes.getStartTime());
        logger.info("End time: {}", workTimes.getEndTime());

        // Calculate total hours worked
        long durationMillis = workTimes.getEndTime() - workTimes.getStartTime();
        logger.info("Duration in milliseconds: {}", durationMillis);

        workTimes.setSumHoursWork(durationMillis); // Store total hours worked

        // Update the work entry
        Database.dao(WorkTimeDAO.class).update(workTimes);

        return workTimes; // Return the updated work entry
    }



    public List<WorkTimes> getAll() {
        return Database.dao(WorkTimeDAO.class).getAll();
    }

    public WorkTimes getById(int id) {
        return Database.dao(WorkTimeDAO.class).getById(id);
    }

    public List<WorkTimes> getAllByDate(long date) {
        return Database.dao(WorkTimeDAO.class).getAllByDate(date);
    }

    public List<WorkTimes> getAllByUserId(int userId) {
        return Database.dao(WorkTimeDAO.class).getAllByUserId(userId);
    }

    public List<WorkTimes> getAllByFullName(String username) {
        return Database.dao(WorkTimeDAO.class).getAllByFullName(username);
    }

    public void delete(int id) {
        Database.dao(WorkTimeDAO.class).delete(id);
    }

    // New method to get work times by userId and date range
    public List<WorkTimes> getWorkTimesByUserIdAndDateRange(int userId, long startDate, long endDate) {
        return Database.dao(WorkTimeDAO.class).getWorkTimesByUserIdAndDateRange(userId, startDate, endDate);
    }
    public List<WorkTimes> getWorkTimesByDateRange(long startRange, long endRange) {
        return Database.dao(WorkTimeDAO.class).getWorkTimesByDateRange( startRange, endRange);
    }
}
