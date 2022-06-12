package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.PhoneNumber;
import com.buchsbaumtax.core.model.SMSLog;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@Dao
public interface PhoneNumberDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO phone_numbers (phone_number, name) VALUES (:phoneNumber, :name)")
    int create(@BindBean PhoneNumber phoneNumber);

    @RegisterFieldMapper(PhoneNumber.class)
    @SqlQuery("SELECT * FROM phone_numbers WHERE phone_number = :phoneNumber")
    PhoneNumber getByNumber(@Bind("phoneNumber") String phoneNumber);

    @RegisterFieldMapper(PhoneNumber.class)
    @SqlQuery("SELECT * FROM phone_numbers WHERE id = :id")
    PhoneNumber get(@Bind("id") int id);

    @SqlUpdate("DELETE FROM phone_numbers WHERE id = :id")
    void delete(@Bind("id") int id);

    @SqlUpdate("INSERT INTO sms_log (phone_number_id, message) VALUES (:phoneNumberId, :message)")
    void createSMSLog(@BindBean SMSLog smsLog);
}
