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
    @SqlUpdate("INSERT INTO logs (alerted, priority, alarm_time, client_id, time_spent, alarm_user_id, alarm_complete, alert, alarm_date, log_date, note, archived) VALUES (:alerted, :priority, :alarmTime, :clientId, :timeSpent, :alarmUserId, :alarmComplete, :alert, :alarmDate, :logDate, :note, :archived)")
    int create(@BindBean Log log);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE id = :id")
    Log get(@Bind("id") int id);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs ORDER BY id")
    List<Log> getAll();

    @SqlUpdate("DELETE FROM logs WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(Log.class)
    @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId")
    List<Log> getForClient(@Bind("clientId") int clientId);
}
