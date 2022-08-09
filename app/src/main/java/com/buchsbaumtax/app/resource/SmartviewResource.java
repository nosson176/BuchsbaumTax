package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.SmartviewCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/smartviews")
public class SmartviewResource {

    @POST
    public SmartviewData createSmartview(@Authenticated User user, SmartviewData smartview) {
        return new SmartviewCRUD().create(user, smartview);
    }

    @GET
    public List<SmartviewData> getUserSmartviews(@Authenticated User user) {
        return new SmartviewCRUD().getForUser(user);
    }

    @PUT
    @Path("/{smartviewId}")
    public SmartviewData updateSmartview(@Authenticated User user, @PathParam("smartviewId") int smartviewId, SmartviewData smartview) {
        return new SmartviewCRUD().update(user, smartviewId, smartview);
    }

    @DELETE
    @Path("/{smartviewId}")
    public BaseResponse deleteSmartview(@PathParam("smartviewId") int smartviewId) {
        Database.dao(SmartviewDAO.class).delete(smartviewId);
        return new BaseResponse(true);
    }
}
