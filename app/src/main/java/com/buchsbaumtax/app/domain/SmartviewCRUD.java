package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class SmartviewCRUD {

    public Smartview create(User user, SmartviewData smartviewData) {
        return Database.dao(SmartviewDAO.class).create(user, smartviewData);
    }

    public List<Smartview> getForUser(User user) {
        return Database.dao(SmartviewDAO.class).getByUser(user.getId());
    }

    public Smartview update(User user, int smartviewId, SmartviewData smartviewData) {
        return Database.dao(SmartviewDAO.class).update(user, smartviewId, smartviewData);
    }
}
