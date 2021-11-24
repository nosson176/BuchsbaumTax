package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.config.Role;
import com.buchsbaumtax.app.domain.user.TimeSlipCRUD;
import com.buchsbaumtax.app.domain.user.UserCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.UpdatePasswordRequest;
import com.buchsbaumtax.core.model.TimeSlip;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.create.UserCreate;
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
    public User createUser(UserCreate userCreate) {
        return new UserCRUD().create(userCreate);
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

    @GET
    @Path("/current/time-slips")
    public List<TimeSlip> getUserTimeSlips(@Authenticated User user) {
        return new TimeSlipCRUD().getUserTimeSlips(user);
    }

    @GET
    @RolesAllowed(Role.ADMIN)
    @Path("/time-slips")
    public List<TimeSlip> getAllTimeSlips() {
        return new TimeSlipCRUD().getAllTimeSlips();
    }

    @POST
    @Path("/current/time-slips")
    public TimeSlip createTimeSlip(@Authenticated User user, TimeSlip timeSlip) {
        return new TimeSlipCRUD().create(user, timeSlip);
    }

    @PUT
    @Path("/current/time-slips/{timeSlipId}")
    public TimeSlip updateTimeSlip(@Authenticated User user, @PathParam("timeSlipId") int timeSlipId, TimeSlip timeSlip) {
        return new TimeSlipCRUD().update(user, timeSlipId, timeSlip);
    }

    @DELETE
    @Path("/current/time-slips/{timeSlipId}")
    public BaseResponse deleteTimeSlip(@Authenticated User user, @PathParam("timeSlipId") int timeSlipId) {
        return new TimeSlipCRUD().delete(user, timeSlipId);
    }
}
