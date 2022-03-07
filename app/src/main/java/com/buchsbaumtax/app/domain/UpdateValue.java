package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.ValueObject;
import com.buchsbaumtax.core.dao.ValueDAO;
import com.buchsbaumtax.core.model.Value;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UpdateValue {

    public ValueObject updateValue(int valueId, Value value) {
        if (valueId != value.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Database.dao(ValueDAO.class).update(value);
        return new ValueObject(Database.dao(ValueDAO.class).get(value.getId()));
    }
}
