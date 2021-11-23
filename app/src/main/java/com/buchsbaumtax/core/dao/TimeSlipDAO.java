package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.TimeSlip;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface TimeSlipDAO {
    @RegisterFieldMapper(TimeSlip.class)
    @SqlQuery("SELECT * FROM time_slips")
    List<TimeSlip> getAll();

    @RegisterFieldMapper(TimeSlip.class)
    @SqlQuery("SELECT * FROM time_slips WHERE user_id = :userId")
    List<TimeSlip> getForUser(@Bind("userId") int userId);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO time_slips (user_id, time_in, time_out, memo) VALUES (:userId, :timeIn, :timeOut, :memo)")
    int create(@BindBean TimeSlip timeSlip);

    @SqlUpdate("UPDATE time_slips SET user_id = :userId, time_in = :timeIn, time_out = :timeOut, memo = :memo WHERE id = :id")
    void update(@BindBean TimeSlip timeSlip);

    @RegisterFieldMapper(TimeSlip.class)
    @SqlQuery("SELECT * FROM time_slips WHERE id = :id")
    TimeSlip get(@Bind("id") int id);

    @SqlUpdate("DELETE FROM time_slips WHERE id = :id")
    void delete(@Bind("id") int id);
}

