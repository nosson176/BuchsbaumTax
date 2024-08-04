package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.WorkTimes;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface WorkTimeDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO work_times (user_id, start_time, end_time, sum_hours_work, username, date) VALUES (:userId, :startTime, :endTime, :sumHoursWork, :username, :date)")
    int create(@BindBean WorkTimes workTimes);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE id = :id")
    WorkTimes getById(@Bind("id") int id);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times ORDER BY date ASC")
    List<WorkTimes> getAll();

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE date = :date ORDER BY start_time ASC")
    List<WorkTimes> getAllByDate(@Bind("date") long date);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE user_id = :userId ORDER BY date ASC")
    List<WorkTimes> getAllByUserId(@Bind("userId") int userId);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE username = :username ORDER BY date DESC")
    List<WorkTimes> getAllByFullName(@Bind("username") String username);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE user_id = :userId AND date = :date ORDER BY start_time ASC")
    WorkTimes getByUserIdAndDate(@Bind("userId") int userId, @Bind("date") long date);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE user_id = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC, start_time ASC")
    List<WorkTimes> getWorkTimesByUserIdAndDateRange(
            @Bind("userId") int userId,
            @Bind("startDate") long startDate,
            @Bind("endDate") long endDate);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE date BETWEEN :startRange AND :endRange ORDER BY username ASC, date ASC, start_time ASC")
    List<WorkTimes> getWorkTimesByDateRange(
            @Bind("startRange") long startRange,
            @Bind("endRange") long endRange);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE user_id = :userId AND date = :date ORDER BY start_time DESC")
    WorkTimes getRecentWorkTimesByUserIdAndDate(@Bind("userId") int userId, @Bind("date") long date);


    @SqlUpdate("UPDATE work_times SET start_time = :startTime, end_time = :endTime, sum_hours_work = :sumHoursWork WHERE id = :id")
    void update(@BindBean WorkTimes workTimes);


    @SqlUpdate("DELETE FROM work_times WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(WorkTimes.class)
    @SqlQuery("SELECT * FROM work_times WHERE user_id = :userId AND date = :date AND end_time = 0 ORDER BY user_id ASC, date ASC, start_time ASC")
    WorkTimes getAllByUserIdAndDateWithNoEndTime(@Bind("userId") int userId, @Bind("date") long date);


}
