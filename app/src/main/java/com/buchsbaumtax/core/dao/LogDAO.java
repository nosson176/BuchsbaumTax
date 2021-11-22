package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface LogDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO logs (client_id, years, alarm_user_id, alert, alarm_complete, alarm_date, alarm_time, log_date, priority, note, seconds_spent, archived, alerted) VALUES (:clientId, :years, :alarmUserId, :alert, :alarmComplete, :alarmDate, :alarmTime, :logDate, :priority, :note, :secondsSpent, :archived, :alerted)")
    int create(@BindBean Log log);

    @SqlUpdate("UPDATE logs SET client_id = :clientId, years = :years, alarm_user_id = :alarmUserId, alert = :alert, alarm_complete = :alarmComplete, alarm_date = :alarmDate, alarm_time = :alarmTime, log_date = :logDate, priority = :priority, note = :note, seconds_spent = :secondsSpent, archived = :archived, alerted = :alerted WHERE id = :id")
    void update(@BindBean Log log);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE id = :id")
    Log get(@Bind("id") int id);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs ORDER BY id")
    List<Log> getAll();

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId")
    List<Log> getForClient(@Bind("clientId") int clientId);
}
