package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.ClientFlagDAO;
import com.buchsbaumtax.core.model.ClientFlag;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UpdateClientFlag {

    public BaseResponse updateClientFlag(User user, ClientFlag clientFlag) {
        if (user.getId() != clientFlag.getUserId()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        Database.dao(ClientFlagDAO.class).updateFlag(clientFlag);
        return new BaseResponse(true);
    }
}
