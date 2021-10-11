package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.CreateValue;
import com.buchsbaumtax.core.dao.ValueDAO;
import com.buchsbaumtax.core.model.Value;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Authenticated
@Path("/values")
public class ValueResource {

    @GET
    public List<Value> getAllValues() {
        return Database.dao(ValueDAO.class).getAll();
    }

    @POST
    public Value createValue(Value value) {
        return new CreateValue().createValue(value);
    }
}
