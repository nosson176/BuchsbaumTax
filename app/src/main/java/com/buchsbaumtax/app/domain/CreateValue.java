package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ValueDAO;
import com.buchsbaumtax.core.model.Value;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class CreateValue {

    public Value createValue(Value value) {
        List<String> valueTypes = Database.dao(ValueDAO.class).getAllValueTypes();

        if (!valueTypes.contains(value.getKey())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        int id = Database.dao(ValueDAO.class).create(value);
        return Database.dao(ValueDAO.class).get(id);
    }
}
