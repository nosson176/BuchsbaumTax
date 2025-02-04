package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Log;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface LogDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO logs (client_id, years, alarm_user_name, alarm_user_id, alert, alarm_complete, alarm_date,alarm_create_change, alarm_time, log_date, priority, note, seconds_spent, archived, alerted, history_log_json, created_by) VALUES (:clientId, :years, :alarmUserName, :alarmUserId, :alert, :alarmComplete, :alarmDate, :alarmCreateChange, :alarmTime, :logDate, :priority, :note, :secondsSpent, :archived, :alerted, :historyLogJson, :createdBy)")
    int create(@BindBean Log log);

    @SqlUpdate("UPDATE logs SET years = :years,alarm_user_name = :alarmUserName, alarm_user_id = :alarmUserId, alert = :alert, alarm_complete = :alarmComplete, alarm_date = :alarmDate,alarm_create_change = :alarmCreateChange, alarm_time = :alarmTime, log_date = :logDate, priority = :priority, note = :note, seconds_spent = :secondsSpent, archived = :archived, alerted = :alerted, history_log_json = :historyLogJson, created_by = :createdBy WHERE id = :id")
    void update(@BindBean Log log);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE id = :id")
    Log get(@Bind("id") int id);

    @SqlUpdate("DELETE FROM logs WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs ORDER BY id")
    List<Log> getAll();

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId ORDER BY log_date DESC, id DESC")
    List<Log> getForClient(@Bind("clientId") int clientId);

    @SqlBatch("UPDATE logs SET years = :years,alarm_user_name = :alarmUserName, alarm_user_id = :alarmUserId, alert = :alert, alarm_complete = :alarmComplete, alarm_date = :alarmDate, alarm_create_change = :alarmCreateChange, alarm_time = :alarmTime, log_date = :logDate, priority = :priority, note = :note, seconds_spent = :secondsSpent, archived = :archived, alerted = :alerted, history_log_json = :historyLogJson, created_by = :createdBy WHERE id = :id")
    void update(@BindBean List<Log> logs);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE TO_TIMESTAMP(alarm_time, 'MM-DD-YYYY HH24:MI') BETWEEN TO_TIMESTAMP(:currentTime, 'MM-DD-YYYY HH24:MI') AND TO_TIMESTAMP(:endOfDay, 'MM-DD-YYYY HH24:MI')")
    List<Log> getLogsBetweenTimes(@Bind("currentTime") String currentTime, @Bind("endOfDay") String endOfDay);

//    @RegisterFieldMapper(Log.class)
//    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId AND created_time >= :threeYearsAgoStart ORDER BY created_time DESC")
//    List<Log> getLogsFromLastThreeYears(@Bind("clientId") int clientId, @Bind("threeYearsAgoStart") long threeYearsAgoStart);
//
//    @RegisterFieldMapper(Log.class)
//    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId AND created_time < :threeYearsAgoStart ORDER BY created_time DESC")
//    List<Log> getLogsBeforeLastThreeYears(@Bind("clientId") int clientId, @Bind("threeYearsAgoStart") long threeYearsAgoStart);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId ORDER BY log_date DESC LIMIT 50")
    List<Log> getNewestLogs(@Bind("clientId") int clientId);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId AND id NOT IN (SELECT id FROM logs WHERE client_id = :clientId ORDER BY log_date DESC LIMIT 50) ORDER BY log_date DESC")
    List<Log> getRemainingLogs(@Bind("clientId") int clientId);



}