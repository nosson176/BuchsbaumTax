package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.ClientFlagDAO;
import com.buchsbaumtax.core.model.ClientFlag;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UpdateClientFlag {
    public BaseResponse updateClientFlag(User user, int clientFlagId, ClientFlag clientFlag) {
        if (clientFlagId != clientFlag.getId() || user.getId() != clientFlag.getUserId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Database.dao(ClientFlagDAO.class).updateFlag(clientFlag);
        return new BaseResponse(true);
    }
}
