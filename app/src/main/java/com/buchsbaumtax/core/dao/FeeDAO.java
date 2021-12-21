package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Fee;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface FeeDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO fees (client_id, year, status, status_detail, fee_type, manual_amount, paid_amount, include, rate, date_fee, sum, archived) VALUES (:clientId, :year, :status, :statusDetail, :feeType, :manualAmount, :paidAmount, :include, :rate, :dateFee, :sum, :archived)")
    int create(@BindBean Fee fee);

    @SqlUpdate("UPDATE fees SET client_id = :clientId, year = :year, status = :status, status_detail = :statusDetail, fee_type = :feeType, manual_amount = :manualAmount, paid_amount = :paidAmount, include = :include, rate = :rate, date_fee = :dateFee, sum = :sum, archived = :archived WHERE id = :id")
    void update(@BindBean Fee fee);

    @RegisterFieldMapper(Fee.class)
    @SqlQuery("SELECT * FROM fees")
    List<Fee> getAll();

    @RegisterFieldMapper(Fee.class)
    @SqlQuery("SELECT * FROM fees WHERE id = :id")
    Fee get(@Bind("id") int id);

    @RegisterFieldMapper(Fee.class)
    @SqlQuery("SELECT * FROM fees WHERE client_id = :clientId ORDER BY date_fee DESC")
    List<Fee> getForClient(@Bind("clientId") int clientId);
}
