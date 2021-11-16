package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Authenticated
@Path("/users")
public class UserResource {
    @GET
    public List<User> getAllUsers() {
        return Database.dao(UserDAO.class).getAll();
    }
}
