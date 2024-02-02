package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.SMSMessage;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@Dao
public interface PhoneNumberDAO {

    @SqlUpdate("INSERT INTO sms_messages (phone_number, message) VALUES (:phoneNumber, :message)")
    void createSMSLog(@BindBean SMSMessage smsMessage);
}
