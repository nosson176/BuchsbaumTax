package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.CreateValue;
import com.buchsbaumtax.app.domain.UpdateValue;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ValueObject;
import com.buchsbaumtax.core.dao.TaxGroupDAO;
import com.buchsbaumtax.core.dao.ValueDAO;
import com.buchsbaumtax.core.model.TaxGroup;
import com.buchsbaumtax.core.model.Value;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Authenticated
@Path("/values")
public class ValueResource {

    @GET
    public Map<String, List<ValueObject>> getAllValues() {
        List<Value> values = Database.dao(ValueDAO.class).getAll();
        return values.stream()
                .collect(Collectors.groupingBy(Value::getKey, HashMap::new, Collectors.mapping(ValueObject::new, Collectors.toList())));
    }

    @POST
    public ValueObject createValue(Value value) {
        return new CreateValue().createValue(value);
    }

    @PUT
    @Path("/{valueId}")
    public ValueObject updateValue(@PathParam("valueId") int valueId, Value value) {
        return new UpdateValue().updateValue(valueId, value);
    }

    @DELETE
    @Path("/{valueId}")
    public BaseResponse deleteValue(@PathParam("valueId") int valueId) {
        Database.dao(ValueDAO.class).delete(valueId);
        return new BaseResponse(true);
    }

    @GET
    @Path("/tax-groups")
    public List<TaxGroup> getAllTaxGroups() {
        return Database.dao(TaxGroupDAO.class).getAll();
    }

    @POST
    @Path("/tax-groups")
    public TaxGroup createTaxGroup(TaxGroup taxGroup) {
        int id = Database.dao(TaxGroupDAO.class).create(taxGroup);
        return Database.dao(TaxGroupDAO.class).get(id);
    }
}
