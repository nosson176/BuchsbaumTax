package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateClient {
    static final Logger logger = LoggerFactory.getLogger(UpdateClient.class);
    public Client updateClient(int clientId, Client client) {
//        logger.info("client: {} client id {}", client,clientId);
        boolean active = true;
        Client oldClient = Database.dao(ClientDAO.class).get(clientId, active);
        if (oldClient == null || clientId != client.getId()) {
            oldClient = Database.dao(ClientDAO.class).get(clientId, false);
            if (oldClient == null || clientId != client.getId()) {

//                logger.info("old client : {}", oldClient);
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }}
            Database.dao(ClientDAO.class).update(client);
            return Database.dao(ClientDAO.class).get(clientId, active);
        }

    public BaseResponse updatePmtStatusClient (int clientId , String pmtStatus ){
        boolean active = true;
        logger.info("status : {}", pmtStatus);
        Client client =  Database.dao(ClientDAO.class).get(clientId ,active);
        if(client != null){
            client.setPmtStatus(pmtStatus);
            Database.dao(ClientDAO.class).update(client);
        logger.info("client : {}", client);
            return  new BaseResponse(true , "pmt status update successfully") ;
        }else {
            // Return error response if client not found
            return new BaseResponse(false , "Client not found or inactive.");
        }
    }
    }


