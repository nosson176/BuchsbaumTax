package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.ValueObject;
import com.buchsbaumtax.core.dao.ValueDAO;
import com.buchsbaumtax.core.model.Value;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class UpdateValue {

    public ValueObject updateValue(int valueId, Value value) {
        if (valueId != value.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Value old = Database.dao(ValueDAO.class).get(valueId);
        if (old.getSortOrder() != value.getSortOrder()) {
            List<Value> values = Database.dao(ValueDAO.class).getByKey(old.getKey());
            reorder(values, old.getSortOrder(), value.getSortOrder());
        }
        Database.dao(ValueDAO.class).update(value);
        return new ValueObject(Database.dao(ValueDAO.class).get(value.getId()));
    }

    private void reorder(List<Value> values, int oldSort, int newSort) {

        // force current order
        for (int i = 0; i < values.size(); i++) {
            values.get(i).setSortOrder(i + 1);
        }

        // reorder
        boolean movedUp = oldSort > newSort;
        for (Value v : values) {
            if (movedUp) {
                if (v.getSortOrder() >= newSort && v.getSortOrder() < oldSort) {
                    v.setSortOrder(v.getSortOrder() + 1);
                }
            }
            else {
                if (v.getSortOrder() > oldSort && v.getSortOrder() <= newSort) {
                    v.setSortOrder(v.getSortOrder() - 1);
                }
            }
        }
        Database.dao(ValueDAO.class).update(values);
    }
}
