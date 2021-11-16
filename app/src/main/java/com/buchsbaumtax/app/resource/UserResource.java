package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.config.Role;
import com.buchsbaumtax.app.domain.UserCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.UpdatePasswordRequest;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/users")
public class UserResource {
    @GET
    public List<User> getAllUsers() {
        return new UserCRUD().getAll();
    }

    @POST
    @RolesAllowed(Role.ADMIN)
    public User createUser(User user) {
        return new UserCRUD().create(user);
    }

    @PUT
    @RolesAllowed(Role.ADMIN)
    @Path("/{userId}")
    public User updateUser(@PathParam("userId") int userId, User user) {
        return new UserCRUD().update(userId, user);
    }

    @DELETE
    @RolesAllowed(Role.ADMIN)
    @Path("/{userId}")
    public BaseResponse deleteUser(@PathParam("userId") int userId) {
        return new UserCRUD().delete(userId);
    }

    @PUT
    @RolesAllowed(Role.ADMIN)
    @Path("/{userId}/password")
    public BaseResponse updatePassword(@PathParam("userId") int userId, UpdatePasswordRequest updatePasswordRequest) {
        return new UserCRUD().updatePassword(userId, updatePasswordRequest);
    }
}
