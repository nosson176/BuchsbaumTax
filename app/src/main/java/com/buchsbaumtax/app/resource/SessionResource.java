package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.user.Login;
import com.buchsbaumtax.app.dto.Credentials;
import com.buchsbaumtax.app.dto.Token;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/sessions")
public class SessionResource {

    @POST
    public Token login(Credentials credentials){ return new Login().login(credentials);}

}
