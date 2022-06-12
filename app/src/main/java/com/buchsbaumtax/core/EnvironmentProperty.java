package com.buchsbaumtax.core;

import com.buchsbaumtax.core.dao.EnvironmentPropertyDAO;
import com.sifradigital.framework.db.Database;

public class EnvironmentProperty {
    public static final String TWILIO_ACCOUNT_SID = "twilio-account-sid";
    public static final String TWILIO_AUTH_TOKEN = "twilio-auth-token";
    public static final String TWILIO_FROM_NUMBER = "twilio-from-number";

    public static String value(String key) {
        return Database.dao(EnvironmentPropertyDAO.class).valueForKey(key);
    }
}
