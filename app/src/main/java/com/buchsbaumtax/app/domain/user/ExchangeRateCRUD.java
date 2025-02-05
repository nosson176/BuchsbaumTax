package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.sifradigital.framework.db.Database;

public class ExchangeRateCRUD {
    public ExchangeRate update(int id ,ExchangeRate exchangeRate){
        int exId = Database.dao(ExchangeRateDAO.class).update(exchangeRate);

        return Database.dao(ExchangeRateDAO.class).getById(exId);

    }
}
